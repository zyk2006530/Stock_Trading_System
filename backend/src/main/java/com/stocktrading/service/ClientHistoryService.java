package com.stocktrading.service;

import com.stocktrading.repository.StockOrderRepository;
import com.stocktrading.repository.TradeRepository;
import com.stocktrading.vo.StockOrderResponse;
import com.stocktrading.vo.TradeResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientHistoryService {
    private final StockOrderRepository stockOrderRepository;
    private final TradeRepository tradeRepository;

    public List<StockOrderResponse> listOrders(Long userId) {
        return stockOrderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(StockOrderResponse::from)
            .toList();
    }

    public List<TradeResponse> listTrades(Long userId) {
        return tradeRepository.findByBuyerUserIdOrSellerUserIdOrderByCreatedAtDesc(userId, userId).stream()
            .map(TradeResponse::from)
            .toList();
    }
}
