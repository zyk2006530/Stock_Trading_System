package com.stocktrading.service;

import com.stocktrading.entity.FundAccount;
import com.stocktrading.entity.Holding;
import com.stocktrading.entity.SecuritiesAccount;
import com.stocktrading.exception.BusinessException;
import com.stocktrading.repository.FundAccountRepository;
import com.stocktrading.repository.FundFlowRepository;
import com.stocktrading.repository.HoldingRepository;
import com.stocktrading.repository.SecuritiesAccountRepository;
import com.stocktrading.repository.StockRepository;
import com.stocktrading.vo.FundAccountDetailResponse;
import com.stocktrading.vo.FundAccountResponse;
import com.stocktrading.vo.FundFlowResponse;
import com.stocktrading.vo.HoldingResponse;
import com.stocktrading.vo.SecuritiesAccountResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientAccountService {
    private final FundAccountRepository fundAccountRepository;
    private final FundFlowRepository fundFlowRepository;
    private final SecuritiesAccountRepository securitiesAccountRepository;
    private final HoldingRepository holdingRepository;
    private final StockRepository stockRepository;

    public FundAccountDetailResponse getFundAccount(Long userId) {
        FundAccount account = fundAccountRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException(404, "fund account not found"));
        List<FundFlowResponse> flows = fundFlowRepository.findByFundAccountIdOrderByCreatedAtDesc(account.getId())
            .stream()
            .map(FundFlowResponse::from)
            .toList();
        return new FundAccountDetailResponse(FundAccountResponse.from(account), flows);
    }

    public SecuritiesAccountResponse getSecuritiesAccount(Long userId) {
        SecuritiesAccount account = securitiesAccountRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException(404, "securities account not found"));
        return SecuritiesAccountResponse.from(account);
    }

    public List<HoldingResponse> getHoldings(Long userId) {
        SecuritiesAccount account = securitiesAccountRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException(404, "securities account not found"));
        List<Holding> holdings = holdingRepository.findBySecuritiesAccountId(account.getId());
        if (holdings.isEmpty()) {
            return List.of();
        }
        List<String> codes = holdings.stream().map(Holding::getStockCode).distinct().toList();
        Map<String, String> nameMap = stockRepository.findByCodeIn(codes).stream()
            .collect(Collectors.toMap(stock -> stock.getCode(), stock -> stock.getName()));
        return holdings.stream()
            .map(holding -> HoldingResponse.from(holding, nameMap.get(holding.getStockCode())))
            .toList();
    }
}
