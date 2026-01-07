package com.Exchange.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Exchange.core.OrderBook;
import com.Exchange.core.OrderBookManager;
import com.Exchange.model.OrderBookSnapShot;
import com.Exchange.model.PriceLevelInfo;
import com.Exchange.response.OrderBookResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderBookService 
{
    private final OrderBookManager manager=new OrderBookManager();
    
    public OrderBook getOrderBook(String baseAsset,String quoteAsset)
    {
        return manager.getOrCreateOrderBook(baseAsset, quoteAsset);
    }

    public List<String> getAllSymbols()
    {
        return manager.getAvailableSymbols();
        
    }

    public OrderBookResponse getOrderBookSnapshot(String baseAsset,String quoteAsset,int depth)
    {
        OrderBookSnapShot snapShot=manager.getSnapShot(baseAsset, quoteAsset, depth);

        int totalBidsVolume=snapShot.getBids().stream().mapToInt(PriceLevelInfo::getTotalQuantity).sum();
        int totalAsksVolume=snapShot.getAsks().stream().mapToInt(PriceLevelInfo::getTotalQuantity).sum();
        return OrderBookResponse.builder()
            .baseAsset(snapShot.getBaseAsset())
            .quoteAsset(snapShot.getQuoteAsset())
            .bids(snapShot.getBids())
            .asks(snapShot.getAsks())
            .lastTradedPrice(snapShot.getLastTradePrice())
            .timeStamp(snapShot.getTimeStamp())
            .totalBidsVolume(totalBidsVolume)
            .totalAsksVolume(totalAsksVolume)
            .bidsDepth(snapShot.getBids().size())
            .asksDepth(snapShot.getAsks().size())
        .build();
    }
    
}
