package com.Exchange.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Exchange.model.Trade;
import com.Exchange.response.OrderBookResponse;
import com.Exchange.response.TradeResponse;

@Service
public class WebSocketMessageService 
{
    @Autowired
    private WebSocketPublisher publisher;

    public  void broadcastTrade(Trade trade)
    {
        TradeResponse response=tradeToTradeResponse(trade);
        publisher.publishTrade(response.getBaseAsset(), response.getQuoteAsset(), response);
    }

    public void orderBookUpdate(OrderBookResponse book)
    {
        publisher.publishOrderBookUpdate(book.getBaseAsset(),book.getQuoteAsset(), book);
    }
    
    private TradeResponse tradeToTradeResponse(Trade trade)
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

}
