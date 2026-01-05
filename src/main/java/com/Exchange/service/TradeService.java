package com.Exchange.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.Exchange.model.Trade;
import com.Exchange.response.TradeResponse;

@Service
public class TradeService 
{
    private final ConcurrentHashMap<String,List<Trade>> tradeHistory=new ConcurrentHashMap<>();
    private static final int MAX_TRADES_PER_SYMBOL=1000;

    public void recordTrade(Trade trade)
    {
        String symbol=getSymbol(trade.getBaseAsset(), trade.getQuoteAsset());
        tradeHistory.computeIfAbsent(symbol, k->new ArrayList<>()).add(trade);

        List<Trade> trades=tradeHistory.get(symbol);
        if(trades.size()>MAX_TRADES_PER_SYMBOL)
        {
            trades.remove(0);
        }
    }

    public void recordTrades(List<Trade> trades)
    {
        trades.forEach(this::recordTrade);
    }

    public List<TradeResponse> getRecentTrades(String baseAsset,String quoteAsset,int limit)
    {
        String symbol=getSymbol(baseAsset, quoteAsset);
        List<Trade> trades=tradeHistory.getOrDefault(symbol,new ArrayList<>());

        List<TradeResponse> responses=trades.stream()
                .sorted((a,b)->Long.compare(b.getTimestamp(), a.getTimestamp()))    
                .map(trade->convertToResponse(trade))
                .collect(Collectors.toList());
        return responses;
    }

    private TradeResponse convertToResponse(Trade trade)
    {
        return TradeResponse.builder()
            .tradeId(trade.getTradeId())
            .buyOrderId(trade.getBuyOrderId())
            .sellOrderId(trade.getSellOrderId())
            .baseAsset(trade.getBaseAsset())
            .quoteAsset(trade.getQuoteAsset())
            .price(trade.getPrice())
            .quantity(trade.getQuantity())
            .timestamp(trade.getTimestamp())
        .build();

    }

    private String getSymbol(String baseAsset,String quoteAsset)
    {
        return baseAsset+"/"+quoteAsset;
    }
    
}
