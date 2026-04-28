package com.stocktrading.service;

import com.stocktrading.dto.SecuritiesAccountCreateRequest;
import com.stocktrading.entity.AccountRelation;
import com.stocktrading.entity.FundAccount;
import com.stocktrading.entity.SecuritiesAccount;
import com.stocktrading.entity.User;
import com.stocktrading.entity.enums.SecuritiesAccountStatus;
import com.stocktrading.exception.BusinessException;
import com.stocktrading.repository.AccountRelationRepository;
import com.stocktrading.repository.FundAccountRepository;
import com.stocktrading.repository.SecuritiesAccountRepository;
import com.stocktrading.repository.UserRepository;
import com.stocktrading.vo.SecuritiesAccountResponse;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffSecuritiesAccountService {
    private final SecuritiesAccountRepository securitiesAccountRepository;
    private final UserRepository userRepository;
    private final FundAccountRepository fundAccountRepository;
    private final AccountRelationRepository accountRelationRepository;

    @Transactional
    public SecuritiesAccountResponse createSecuritiesAccount(SecuritiesAccountCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new BusinessException(404, "user not found"));
        if (securitiesAccountRepository.findByUserId(user.getId()).isPresent()) {
            throw new BusinessException(400, "securities account already exists");
        }

        SecuritiesAccount account = SecuritiesAccount.builder()
            .accountNo(generateAccountNo())
            .userId(user.getId())
            .status(SecuritiesAccountStatus.NORMAL)
            .build();
        securitiesAccountRepository.save(account);

        if (request.getFundAccountId() != null) {
            FundAccount fundAccount = fundAccountRepository.findById(request.getFundAccountId())
                .orElseThrow(() -> new BusinessException(404, "fund account not found"));
            if (!fundAccount.getUserId().equals(user.getId())) {
                throw new BusinessException(400, "account owner mismatch");
            }
            accountRelationRepository
                .findByFundAccountIdAndSecuritiesAccountId(fundAccount.getId(), account.getId())
                .orElseGet(() -> accountRelationRepository.save(AccountRelation.builder()
                    .fundAccountId(fundAccount.getId())
                    .securitiesAccountId(account.getId())
                    .build()));
        }

        return SecuritiesAccountResponse.from(account);
    }

    @Transactional
    public SecuritiesAccountResponse markLost(Long id) {
        SecuritiesAccount account = getAccount(id);
        if (account.getStatus() != SecuritiesAccountStatus.NORMAL) {
            throw new BusinessException(400, "securities account not active");
        }
        account.setStatus(SecuritiesAccountStatus.LOST);
        securitiesAccountRepository.save(account);
        return SecuritiesAccountResponse.from(account);
    }

    @Transactional
    public SecuritiesAccountResponse reopen(Long id) {
        SecuritiesAccount account = getAccount(id);
        if (account.getStatus() != SecuritiesAccountStatus.LOST) {
            throw new BusinessException(400, "securities account not lost");
        }
        account.setStatus(SecuritiesAccountStatus.NORMAL);
        securitiesAccountRepository.save(account);
        return SecuritiesAccountResponse.from(account);
    }

    @Transactional
    public SecuritiesAccountResponse close(Long id) {
        SecuritiesAccount account = getAccount(id);
        if (account.getStatus() == SecuritiesAccountStatus.CLOSED) {
            throw new BusinessException(400, "securities account already closed");
        }
        account.setStatus(SecuritiesAccountStatus.CLOSED);
        securitiesAccountRepository.save(account);
        return SecuritiesAccountResponse.from(account);
    }

    public SecuritiesAccountResponse getById(Long id) {
        return SecuritiesAccountResponse.from(getAccount(id));
    }

    private SecuritiesAccount getAccount(Long id) {
        return securitiesAccountRepository.findById(id)
            .orElseThrow(() -> new BusinessException(404, "securities account not found"));
    }

    private String generateAccountNo() {
        String accountNo;
        do {
            int suffix = ThreadLocalRandom.current().nextInt(1000, 9999);
            accountNo = "SA-" + System.currentTimeMillis() + "-" + suffix;
        } while (securitiesAccountRepository.existsByAccountNo(accountNo));
        return accountNo;
    }
}
