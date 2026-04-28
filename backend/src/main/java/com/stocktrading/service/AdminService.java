package com.stocktrading.service;

import com.stocktrading.entity.Holding;
import com.stocktrading.entity.StockOrder;
import com.stocktrading.entity.Trade;
import com.stocktrading.entity.enums.OrderDirection;
import com.stocktrading.repository.FundAccountRepository;
import com.stocktrading.repository.HoldingRepository;
import com.stocktrading.repository.SecuritiesAccountRepository;
import com.stocktrading.repository.StockOrderRepository;
import com.stocktrading.repository.StockRepository;
import com.stocktrading.repository.TradeRepository;
import com.stocktrading.repository.UserRepository;
import com.stocktrading.vo.AdminAccountOverviewResponse;
import com.stocktrading.vo.AdminOrderResponse;
import com.stocktrading.vo.AdminOrderStatsResponse;
import com.stocktrading.vo.AdminTradeResponse;
import com.stocktrading.vo.FundAccountResponse;
import com.stocktrading.vo.HoldingResponse;
import com.stocktrading.vo.SecuritiesAccountResponse;
import com.stocktrading.vo.UserResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final StockOrderRepository stockOrderRepository;
    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final FundAccountRepository fundAccountRepository;
    private final SecuritiesAccountRepository securitiesAccountRepository;
    private final HoldingRepository holdingRepository;
    private final StockRepository stockRepository;

    public List<AdminOrderResponse> listOrders(String stockCode) {
        List<StockOrder> orders = (stockCode == null || stockCode.isBlank())
            ? stockOrderRepository.findAllByOrderByCreatedAtDesc()
            : stockOrderRepository.findByStockCodeOrderByCreatedAtDesc(stockCode.trim());
        return orders.stream().map(AdminOrderResponse::from).toList();
    }

    public List<AdminTradeResponse> listTrades() {
        List<Trade> trades = tradeRepository.findAllByOrderByCreatedAtDesc();
        return trades.stream().map(AdminTradeResponse::from).toList();
    }

    public List<AdminOrderStatsResponse> orderStats() {
        List<StockOrder> orders = stockOrderRepository.findAll();
        Map<String, StatsBucket> buckets = new HashMap<>();
        for (StockOrder order : orders) {
            StatsBucket bucket = buckets.computeIfAbsent(order.getStockCode(), key -> new StatsBucket());
            if (order.getDirection() == OrderDirection.BUY) {
                bucket.buyOrders++;
                bucket.buyQuantity += order.getQuantity();
            } else {
                bucket.sellOrders++;
                bucket.sellQuantity += order.getQuantity();
            }
        }
        List<AdminOrderStatsResponse> responses = new ArrayList<>();
        for (Map.Entry<String, StatsBucket> entry : buckets.entrySet()) {
            StatsBucket bucket = entry.getValue();
            responses.add(new AdminOrderStatsResponse(
                entry.getKey(),
                bucket.buyOrders,
                bucket.sellOrders,
                bucket.buyQuantity,
                bucket.sellQuantity
            ));
        }
        return responses.stream()
            .sorted((a, b) -> a.getStockCode().compareToIgnoreCase(b.getStockCode()))
            .toList();
    }

    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    public AdminAccountOverviewResponse listAccounts() {
        List<FundAccountResponse> fundAccounts = fundAccountRepository.findAll().stream()
            .map(FundAccountResponse::from)
            .toList();
        List<SecuritiesAccountResponse> securitiesAccounts = securitiesAccountRepository.findAll().stream()
            .map(SecuritiesAccountResponse::from)
            .toList();

        List<Holding> holdings = holdingRepository.findAll();
        Map<String, String> nameMap;
        if (holdings.isEmpty()) {
            nameMap = Map.of();
        } else {
            nameMap = stockRepository.findByCodeIn(
                holdings.stream().map(Holding::getStockCode).distinct().toList())
                .stream()
                .collect(Collectors.toMap(stock -> stock.getCode(), stock -> stock.getName()));
        }
        List<HoldingResponse> holdingResponses = holdings.stream()
            .map(holding -> HoldingResponse.from(holding, nameMap.get(holding.getStockCode())))
            .toList();

        return new AdminAccountOverviewResponse(fundAccounts, securitiesAccounts, holdingResponses);
    }

    private static class StatsBucket {
        private long buyOrders;
        private long sellOrders;
        private long buyQuantity;
        private long sellQuantity;
    }
}
