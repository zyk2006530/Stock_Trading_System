package com.stocktrading.service;

import com.stocktrading.entity.FundAccount;
import com.stocktrading.entity.FundFlow;
import com.stocktrading.entity.Holding;
import com.stocktrading.entity.SecuritiesAccount;
import com.stocktrading.entity.Stock;
import com.stocktrading.entity.StockOrder;
import com.stocktrading.entity.Trade;
import com.stocktrading.entity.enums.FundFlowType;
import com.stocktrading.entity.enums.OrderDirection;
import com.stocktrading.entity.enums.OrderStatus;
import com.stocktrading.exception.BusinessException;
import com.stocktrading.repository.FundAccountRepository;
import com.stocktrading.repository.FundFlowRepository;
import com.stocktrading.repository.HoldingRepository;
import com.stocktrading.repository.SecuritiesAccountRepository;
import com.stocktrading.repository.StockOrderRepository;
import com.stocktrading.repository.StockRepository;
import com.stocktrading.repository.TradeRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final StockOrderRepository stockOrderRepository;
    private final TradeRepository tradeRepository;
    private final FundAccountRepository fundAccountRepository;
    private final SecuritiesAccountRepository securitiesAccountRepository;
    private final HoldingRepository holdingRepository;
    private final FundFlowRepository fundFlowRepository;
    private final StockRepository stockRepository;

    @Transactional
    public void match(String stockCode) {
        List<OrderStatus> activeStatuses = List.of(OrderStatus.PENDING, OrderStatus.PARTIAL_FILLED);
        // Price-time priority: buy orders sort by price desc, time asc; sell orders sort by price asc, time asc.
        List<StockOrder> buyOrders = stockOrderRepository
            .findByStockCodeAndDirectionAndStatusInOrderByPriceDescCreatedAtAsc(
                stockCode, OrderDirection.BUY, activeStatuses);
        List<StockOrder> sellOrders = stockOrderRepository
            .findByStockCodeAndDirectionAndStatusInOrderByPriceAscCreatedAtAsc(
                stockCode, OrderDirection.SELL, activeStatuses);

        int buyIndex = 0;
        int sellIndex = 0;
        while (buyIndex < buyOrders.size() && sellIndex < sellOrders.size()) {
            StockOrder buyOrder = buyOrders.get(buyIndex);
            StockOrder sellOrder = sellOrders.get(sellIndex);

            if (buyOrder.getPrice().compareTo(sellOrder.getPrice()) < 0) {
                break;
            }

            int tradeQuantity = Math.min(buyOrder.getRemainingQuantity(), sellOrder.getRemainingQuantity());
            // Trade price uses the sell order price.
            BigDecimal tradePrice = sellOrder.getPrice();

            executeTrade(buyOrder, sellOrder, tradeQuantity, tradePrice);

            if (buyOrder.getRemainingQuantity() == 0) {
                buyIndex++;
            }
            if (sellOrder.getRemainingQuantity() == 0) {
                sellIndex++;
            }
        }
    }

    private void executeTrade(StockOrder buyOrder, StockOrder sellOrder, int tradeQuantity, BigDecimal tradePrice) {
        BigDecimal tradeAmount = tradePrice.multiply(BigDecimal.valueOf(tradeQuantity))
            .setScale(2, RoundingMode.HALF_UP);

        FundAccount buyerFund = fundAccountRepository.findByUserId(buyOrder.getUserId())
            .orElseThrow(() -> new BusinessException(404, "buyer fund account not found"));
        FundAccount sellerFund = fundAccountRepository.findByUserId(sellOrder.getUserId())
            .orElseThrow(() -> new BusinessException(404, "seller fund account not found"));
        SecuritiesAccount buyerAccount = securitiesAccountRepository.findByUserId(buyOrder.getUserId())
            .orElseThrow(() -> new BusinessException(404, "buyer securities account not found"));
        SecuritiesAccount sellerAccount = securitiesAccountRepository.findByUserId(sellOrder.getUserId())
            .orElseThrow(() -> new BusinessException(404, "seller securities account not found"));

        BigDecimal expectedAmount = buyOrder.getPrice().multiply(BigDecimal.valueOf(tradeQuantity))
            .setScale(2, RoundingMode.HALF_UP);
        buyerFund.setFrozenBalance(buyerFund.getFrozenBalance().subtract(expectedAmount));
        buyerFund.setBalance(buyerFund.getBalance().add(expectedAmount.subtract(tradeAmount)));
        fundAccountRepository.save(buyerFund);

        sellerFund.setBalance(sellerFund.getBalance().add(tradeAmount));
        fundAccountRepository.save(sellerFund);

        Holding buyerHolding = holdingRepository
            .findBySecuritiesAccountIdAndStockCode(buyerAccount.getId(), buyOrder.getStockCode())
            .orElseGet(() -> holdingRepository.save(Holding.builder()
                .userId(buyerAccount.getUserId())
                .securitiesAccountId(buyerAccount.getId())
                .stockCode(buyOrder.getStockCode())
                .quantity(0)
                .frozenQuantity(0)
                .build()));
        buyerHolding.setQuantity(buyerHolding.getQuantity() + tradeQuantity);
        holdingRepository.save(buyerHolding);

        Holding sellerHolding = holdingRepository
            .findBySecuritiesAccountIdAndStockCode(sellerAccount.getId(), sellOrder.getStockCode())
            .orElseThrow(() -> new BusinessException(404, "seller holding not found"));
        sellerHolding.setFrozenQuantity(sellerHolding.getFrozenQuantity() - tradeQuantity);
        holdingRepository.save(sellerHolding);

        buyOrder.setFilledQuantity(buyOrder.getFilledQuantity() + tradeQuantity);
        buyOrder.setRemainingQuantity(buyOrder.getRemainingQuantity() - tradeQuantity);
        buyOrder.setStatus(resolveStatus(buyOrder));
        stockOrderRepository.save(buyOrder);

        sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + tradeQuantity);
        sellOrder.setRemainingQuantity(sellOrder.getRemainingQuantity() - tradeQuantity);
        sellOrder.setStatus(resolveStatus(sellOrder));
        stockOrderRepository.save(sellOrder);

        Trade trade = Trade.builder()
            .tradeNo(generateTradeNo())
            .stockCode(buyOrder.getStockCode())
            .buyOrderId(buyOrder.getId())
            .sellOrderId(sellOrder.getId())
            .buyerUserId(buyOrder.getUserId())
            .sellerUserId(sellOrder.getUserId())
            .price(tradePrice)
            .quantity(tradeQuantity)
            .amount(tradeAmount)
            .build();
        tradeRepository.save(trade);

        fundFlowRepository.save(FundFlow.builder()
            .fundAccountId(buyerFund.getId())
            .userId(buyerFund.getUserId())
            .type(FundFlowType.TRADE_BUY)
            .amount(tradeAmount)
            .balanceAfter(buyerFund.getBalance())
            .remark("Trade buy")
            .build());

        fundFlowRepository.save(FundFlow.builder()
            .fundAccountId(sellerFund.getId())
            .userId(sellerFund.getUserId())
            .type(FundFlowType.TRADE_SELL)
            .amount(tradeAmount)
            .balanceAfter(sellerFund.getBalance())
            .remark("Trade sell")
            .build());

        Stock stock = stockRepository.findByCode(buyOrder.getStockCode())
            .orElseThrow(() -> new BusinessException(404, "stock not found"));
        stock.setLatestPrice(tradePrice);
        stockRepository.save(stock);
    }

    private OrderStatus resolveStatus(StockOrder order) {
        if (order.getRemainingQuantity() == 0) {
            return OrderStatus.FILLED;
        }
        if (order.getFilledQuantity() > 0) {
            return OrderStatus.PARTIAL_FILLED;
        }
        return OrderStatus.PENDING;
    }

    private String generateTradeNo() {
        int suffix = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "TRD-" + System.currentTimeMillis() + "-" + suffix;
    }
}
