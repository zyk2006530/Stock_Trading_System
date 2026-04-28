package com.stocktrading.service;

import com.stocktrading.dto.FundAccountAmountRequest;
import com.stocktrading.dto.FundAccountCreateRequest;
import com.stocktrading.dto.FundAccountPasswordUpdateRequest;
import com.stocktrading.entity.AccountApplication;
import com.stocktrading.entity.FundAccount;
import com.stocktrading.entity.FundFlow;
import com.stocktrading.entity.User;
import com.stocktrading.entity.enums.AccountApplicationStatus;
import com.stocktrading.entity.enums.AccountApplicationType;
import com.stocktrading.entity.enums.FundAccountStatus;
import com.stocktrading.entity.enums.FundFlowType;
import com.stocktrading.exception.BusinessException;
import com.stocktrading.repository.AccountApplicationRepository;
import com.stocktrading.repository.FundAccountRepository;
import com.stocktrading.repository.FundFlowRepository;
import com.stocktrading.repository.UserRepository;
import com.stocktrading.vo.FundAccountDetailResponse;
import com.stocktrading.vo.FundAccountResponse;
import com.stocktrading.vo.FundFlowResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffFundAccountService {
    private final FundAccountRepository fundAccountRepository;
    private final UserRepository userRepository;
    private final AccountApplicationRepository accountApplicationRepository;
    private final FundFlowRepository fundFlowRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public FundAccountResponse createFundAccount(FundAccountCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new BusinessException(404, "user not found"));
        if (fundAccountRepository.findByUserId(user.getId()).isPresent()) {
            throw new BusinessException(400, "fund account already exists");
        }
        BigDecimal initialBalance = request.getInitialBalance() == null
            ? BigDecimal.ZERO
            : request.getInitialBalance();
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(400, "initial balance must be >= 0");
        }

        FundAccount account = FundAccount.builder()
            .accountNo(generateAccountNo())
            .userId(user.getId())
            .password(passwordEncoder.encode(request.getPassword()))
            .balance(initialBalance)
            .frozenBalance(BigDecimal.ZERO)
            .status(FundAccountStatus.PENDING)
            .build();
        fundAccountRepository.save(account);

        AccountApplication application = AccountApplication.builder()
            .userId(user.getId())
            .type(AccountApplicationType.FUND_ACCOUNT)
            .status(AccountApplicationStatus.PENDING)
            .build();
        accountApplicationRepository.save(application);

        return FundAccountResponse.from(account);
    }

    @Transactional
    public FundAccountResponse approveFundAccount(Long id) {
        FundAccount account = getFundAccount(id);
        if (account.getStatus() != FundAccountStatus.PENDING) {
            throw new BusinessException(400, "fund account not pending");
        }
        account.setStatus(FundAccountStatus.NORMAL);
        fundAccountRepository.save(account);
        accountApplicationRepository
            .findTopByUserIdAndTypeAndStatusOrderByCreatedAtDesc(
                account.getUserId(), AccountApplicationType.FUND_ACCOUNT, AccountApplicationStatus.PENDING)
            .ifPresent(application -> {
                application.setStatus(AccountApplicationStatus.APPROVED);
                accountApplicationRepository.save(application);
            });
        return FundAccountResponse.from(account);
    }

    @Transactional
    public FundAccountResponse updatePassword(Long id, FundAccountPasswordUpdateRequest request) {
        FundAccount account = getFundAccount(id);
        if (account.getStatus() == FundAccountStatus.CLOSED) {
            throw new BusinessException(400, "fund account closed");
        }
        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        fundAccountRepository.save(account);
        return FundAccountResponse.from(account);
    }

    @Transactional
    public FundAccountResponse deposit(Long id, FundAccountAmountRequest request) {
        FundAccount account = getFundAccount(id);
        requireNormal(account);
        BigDecimal amount = request.getAmount();
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        fundAccountRepository.save(account);
        createFlow(account, FundFlowType.DEPOSIT, amount, newBalance, request.getRemark());
        return FundAccountResponse.from(account);
    }

    @Transactional
    public FundAccountResponse withdraw(Long id, FundAccountAmountRequest request) {
        FundAccount account = getFundAccount(id);
        requireNormal(account);
        BigDecimal amount = request.getAmount();
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BusinessException(400, "insufficient balance");
        }
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        fundAccountRepository.save(account);
        createFlow(account, FundFlowType.WITHDRAW, amount, newBalance, request.getRemark());
        return FundAccountResponse.from(account);
    }

    @Transactional
    public FundAccountResponse markLost(Long id) {
        FundAccount account = getFundAccount(id);
        if (account.getStatus() != FundAccountStatus.NORMAL) {
            throw new BusinessException(400, "fund account not active");
        }
        account.setStatus(FundAccountStatus.LOST);
        fundAccountRepository.save(account);
        return FundAccountResponse.from(account);
    }

    @Transactional
    public FundAccountResponse reopen(Long id) {
        FundAccount account = getFundAccount(id);
        if (account.getStatus() != FundAccountStatus.LOST) {
            throw new BusinessException(400, "fund account not lost");
        }
        account.setStatus(FundAccountStatus.NORMAL);
        fundAccountRepository.save(account);
        return FundAccountResponse.from(account);
    }

    @Transactional
    public FundAccountResponse close(Long id) {
        FundAccount account = getFundAccount(id);
        if (account.getStatus() == FundAccountStatus.CLOSED) {
            throw new BusinessException(400, "fund account already closed");
        }
        account.setStatus(FundAccountStatus.CLOSED);
        fundAccountRepository.save(account);
        return FundAccountResponse.from(account);
    }

    public FundAccountDetailResponse getDetail(Long id) {
        FundAccount account = getFundAccount(id);
        List<FundFlowResponse> flows = fundFlowRepository.findByFundAccountIdOrderByCreatedAtDesc(id).stream()
            .map(FundFlowResponse::from)
            .toList();
        return new FundAccountDetailResponse(FundAccountResponse.from(account), flows);
    }

    private FundAccount getFundAccount(Long id) {
        return fundAccountRepository.findById(id)
            .orElseThrow(() -> new BusinessException(404, "fund account not found"));
    }

    private void requireNormal(FundAccount account) {
        if (account.getStatus() != FundAccountStatus.NORMAL) {
            throw new BusinessException(400, "fund account not active");
        }
    }

    private void createFlow(FundAccount account, FundFlowType type, BigDecimal amount, BigDecimal balanceAfter,
        String remark) {
        FundFlow flow = FundFlow.builder()
            .fundAccountId(account.getId())
            .userId(account.getUserId())
            .type(type)
            .amount(amount)
            .balanceAfter(balanceAfter)
            .remark(remark)
            .build();
        fundFlowRepository.save(flow);
    }

    private String generateAccountNo() {
        String accountNo;
        do {
            int suffix = ThreadLocalRandom.current().nextInt(1000, 9999);
            accountNo = "FA-" + System.currentTimeMillis() + "-" + suffix;
        } while (fundAccountRepository.existsByAccountNo(accountNo));
        return accountNo;
    }
}
