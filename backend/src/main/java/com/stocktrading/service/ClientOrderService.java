package com.stocktrading.service;

import com.stocktrading.dto.OrderCreateRequest;
import com.stocktrading.entity.FundAccount;
import com.stocktrading.entity.Holding;
import com.stocktrading.entity.SecuritiesAccount;
import com.stocktrading.entity.Stock;
import com.stocktrading.entity.StockOrder;
import com.stocktrading.entity.enums.FundAccountStatus;
import com.stocktrading.entity.enums.OrderDirection;
import com.stocktrading.entity.enums.OrderStatus;
import com.stocktrading.entity.enums.SecuritiesAccountStatus;
import com.stocktrading.entity.enums.StockStatus;
import com.stocktrading.exception.BusinessException;
import com.stocktrading.repository.FundAccountRepository;
import com.stocktrading.repository.HoldingRepository;
import com.stocktrading.repository.SecuritiesAccountRepository;
import com.stocktrading.repository.StockOrderRepository;
import com.stocktrading.repository.StockRepository;
import com.stocktrading.vo.StockOrderResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientOrderService {
    private final FundAccountRepository fundAccountRepository;
    private final SecuritiesAccountRepository securitiesAccountRepository;
    private final HoldingRepository holdingRepository;
    private final StockRepository stockRepository;
    private final StockOrderRepository stockOrderRepository;
    private final MatchingService matchingService;

    @Transactional
    public StockOrderResponse buy(Long userId, OrderCreateRequest request) {
        String stockCode = normalizeStockCode(request.getStockCode());
        Stock stock = getActiveStock(stockCode);
        FundAccount fundAccount = getFundAccount(userId);
        SecuritiesAccount securitiesAccount = getSecuritiesAccount(userId);
        ensureTradingAllowed(fundAccount, securitiesAccount);

        BigDecimal amount = calculateAmount(request.getPrice(), request.getQuantity());
        if (fundAccount.getBalance().compareTo(amount) < 0) {
            throw new BusinessException(400, "insufficient balance");
        }
        fundAccount.setBalance(fundAccount.getBalance().subtract(amount));
        fundAccount.setFrozenBalance(fundAccount.getFrozenBalance().add(amount));
        fundAccountRepository.save(fundAccount);

        StockOrder order = buildOrder(userId, stock.getCode(), OrderDirection.BUY, request.getPrice(),
            request.getQuantity());
        stockOrderRepository.save(order);
        matchingService.match(stock.getCode());
        return StockOrderResponse.from(order);
    }

    @Transactional
    public StockOrderResponse sell(Long userId, OrderCreateRequest request) {
        String stockCode = normalizeStockCode(request.getStockCode());
        getActiveStock(stockCode);
        FundAccount fundAccount = getFundAccount(userId);
        SecuritiesAccount securitiesAccount = getSecuritiesAccount(userId);
        ensureTradingAllowed(fundAccount, securitiesAccount);

        Holding holding = holdingRepository.findBySecuritiesAccountIdAndStockCode(securitiesAccount.getId(), stockCode)
            .orElseThrow(() -> new BusinessException(400, "holding not found"));
        if (holding.getQuantity() < request.getQuantity()) {
            throw new BusinessException(400, "insufficient holding");
        }
        holding.setQuantity(holding.getQuantity() - request.getQuantity());
        holding.setFrozenQuantity(holding.getFrozenQuantity() + request.getQuantity());
        holdingRepository.save(holding);

        StockOrder order = buildOrder(userId, stockCode, OrderDirection.SELL, request.getPrice(), request.getQuantity());
        stockOrderRepository.save(order);
        matchingService.match(stockCode);
        return StockOrderResponse.from(order);
    }

    private FundAccount getFundAccount(Long userId) {
        return fundAccountRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException(404, "fund account not found"));
    }

    private SecuritiesAccount getSecuritiesAccount(Long userId) {
        return securitiesAccountRepository.findByUserId(userId)
            .orElseThrow(() -> new BusinessException(404, "securities account not found"));
    }

    private void ensureTradingAllowed(FundAccount fundAccount, SecuritiesAccount securitiesAccount) {
        if (fundAccount.getStatus() != FundAccountStatus.NORMAL) {
            throw new BusinessException(400, "fund account not active");
        }
        if (securitiesAccount.getStatus() != SecuritiesAccountStatus.NORMAL) {
            throw new BusinessException(400, "securities account not active");
        }
    }

    private Stock getActiveStock(String stockCode) {
        Stock stock = stockRepository.findByCode(stockCode)
            .orElseThrow(() -> new BusinessException(404, "stock not found"));
        if (stock.getStatus() != StockStatus.ACTIVE) {
            throw new BusinessException(400, "stock not active");
        }
        return stock;
    }

    private String normalizeStockCode(String stockCode) {
        if (stockCode == null || stockCode.isBlank()) {
            throw new BusinessException(400, "stock code required");
        }
        return stockCode.trim();
    }

    private BigDecimal calculateAmount(BigDecimal price, Integer quantity) {
        return price.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    private StockOrder buildOrder(Long userId, String stockCode, OrderDirection direction, BigDecimal price,
        Integer quantity) {
        return StockOrder.builder()
            .orderNo(generateOrderNo())
            .userId(userId)
            .stockCode(stockCode)
            .direction(direction)
            .price(price)
            .quantity(quantity)
            .filledQuantity(0)
            .remainingQuantity(quantity)
            .status(OrderStatus.PENDING)
            .build();
    }

    private String generateOrderNo() {
        int suffix = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "ORD-" + System.currentTimeMillis() + "-" + suffix;
    }
}
