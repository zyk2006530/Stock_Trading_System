package com.stocktrading.service;

import com.stocktrading.dto.AccountRelationRequest;
import com.stocktrading.entity.AccountRelation;
import com.stocktrading.entity.FundAccount;
import com.stocktrading.entity.SecuritiesAccount;
import com.stocktrading.exception.BusinessException;
import com.stocktrading.repository.AccountRelationRepository;
import com.stocktrading.repository.FundAccountRepository;
import com.stocktrading.repository.SecuritiesAccountRepository;
import com.stocktrading.vo.AccountRelationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffAccountRelationService {
    private final AccountRelationRepository accountRelationRepository;
    private final FundAccountRepository fundAccountRepository;
    private final SecuritiesAccountRepository securitiesAccountRepository;

    @Transactional
    public AccountRelationResponse link(AccountRelationRequest request) {
        FundAccount fundAccount = fundAccountRepository.findById(request.getFundAccountId())
            .orElseThrow(() -> new BusinessException(404, "fund account not found"));
        SecuritiesAccount securitiesAccount = securitiesAccountRepository.findById(request.getSecuritiesAccountId())
            .orElseThrow(() -> new BusinessException(404, "securities account not found"));
        if (!fundAccount.getUserId().equals(securitiesAccount.getUserId())) {
            throw new BusinessException(400, "account owner mismatch");
        }
        AccountRelation relation = accountRelationRepository
            .findByFundAccountIdAndSecuritiesAccountId(fundAccount.getId(), securitiesAccount.getId())
            .orElseGet(() -> accountRelationRepository.save(AccountRelation.builder()
                .fundAccountId(fundAccount.getId())
                .securitiesAccountId(securitiesAccount.getId())
                .build()));
        return AccountRelationResponse.from(relation);
    }
}
