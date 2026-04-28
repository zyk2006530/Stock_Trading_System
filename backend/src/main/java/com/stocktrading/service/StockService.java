package com.stocktrading.service;

import com.stocktrading.entity.Stock;
import com.stocktrading.entity.Trade;
import com.stocktrading.exception.BusinessException;
import com.stocktrading.repository.StockRepository;
import com.stocktrading.repository.TradeRepository;
import com.stocktrading.vo.StockResponse;
import com.stocktrading.vo.StockStatsResponse;
import com.stocktrading.vo.TradeResponse;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final TradeRepository tradeRepository;

    public List<StockResponse> listAll() {
        return stockRepository.findAll().stream().map(StockResponse::from).toList();
    }

    public StockResponse getByCode(String code) {
        Stock stock = stockRepository.findByCode(code)
            .orElseThrow(() -> new BusinessException(404, "stock not found"));
        return StockResponse.from(stock);
    }

    public List<StockResponse> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return listAll();
        }
        return stockRepository.findByCodeContainingOrNameContainingIgnoreCase(keyword.trim(), keyword.trim())
            .stream()
            .map(StockResponse::from)
            .toList();
    }

    public List<TradeResponse> recentTrades(String code) {
        ensureStockExists(code);
        return tradeRepository.findTop20ByStockCodeOrderByCreatedAtDesc(code)
            .stream()
            .map(TradeResponse::from)
            .toList();
    }

    public StockStatsResponse stats(String code) {
        Stock stock = stockRepository.findByCode(code)
            .orElseThrow(() -> new BusinessException(404, "stock not found"));
        List<Trade> trades = tradeRepository.findByStockCodeOrderByCreatedAtDesc(code);
        if (trades.isEmpty()) {
            BigDecimal price = stock.getLatestPrice();
            return new StockStatsResponse(code, price, price, price, 0L, BigDecimal.ZERO);
        }
        BigDecimal latestPrice = trades.get(0).getPrice();
        BigDecimal highPrice = trades.stream().map(Trade::getPrice).max(Comparator.naturalOrder()).orElse(latestPrice);
        BigDecimal lowPrice = trades.stream().map(Trade::getPrice).min(Comparator.naturalOrder()).orElse(latestPrice);
        long totalVolume = trades.stream().mapToLong(Trade::getQuantity).sum();
        BigDecimal totalAmount = trades.stream()
            .map(Trade::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new StockStatsResponse(code, latestPrice, highPrice, lowPrice, totalVolume, totalAmount);
    }

    private void ensureStockExists(String code) {
        if (!stockRepository.existsByCode(code)) {
            throw new BusinessException(404, "stock not found");
        }
    }
}
