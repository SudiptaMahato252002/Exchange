package com.Exchange.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Exchange.core.OrderBook;
import com.Exchange.message.OrderMessage;
import com.Exchange.message.TradeMessage;
import com.Exchange.model.Order;
import com.Exchange.model.Trade;
import com.Exchange.producer.TradeProducer;
import com.Exchange.websocket.WebSocketMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaOrderProcessingService 
{
    @Autowired
    private OrderBookService orderBookService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private TradeProducer tradeProducer;
    @Autowired
    private WebSocketMessageService websocketService;
    @Autowired
    private CandleService candleService;

    public void processOrder(OrderMessage orderMessage)
    {
        Order order=convertToOrder(orderMessage);
        OrderBook orderbook=orderBookService.getOrderBook(order.getBaseAsset(), order.getQuoteAsset());
        List<Trade> trades=orderbook.addOrder(order);
        tradeService.recordTrades(trades);

        trades.forEach(candleService::processTrade);
        List<TradeMessage> tradeMessages=trades.stream().map(this::convertToTradeMessage).collect(Collectors.toList());
        
        
        trades.forEach(websocketService::broadcastTrade);

        var orderBookSnapshot = orderBookService.getOrderBookSnapshot(
            orderMessage.getBaseAsset(), 
            orderMessage.getQuoteAsset(), 
            10
        );
        websocketService.orderBookUpdate(orderBookSnapshot);
        tradeProducer.sendTrades(tradeMessages);
        log.info("Order processed: orderId={}, trades={}", 
            orderMessage.getOrderId(), trades.size());
    }

     private Order convertToOrder(OrderMessage message) 
    {
        return Order.builder()
            .orderId(message.getOrderId())
            .userId(message.getUserId())
            .baseAsset(message.getBaseAsset())
            .quoteAsset(message.getQuoteAsset())
            .side(message.getSide())
            .type(message.getType())
            .price(message.getPrice())
            .quantity(message.getQuantity())
            .timeStamp(message.getTimeStamp())
            .build();
    }

    private TradeMessage convertToTradeMessage(Trade trade) 
    {
        return TradeMessage.builder()
            .tradeId(trade.getTradeId())
            .buyOrderId(trade.getBuyOrderId())
            .sellOrderId(trade.getSellOrderId())
            .baseAsset(trade.getBaseAsset())
            .quoteAsset(trade.getQuoteAsset())
            .price(trade.getPrice())
            .quantity(trade.getQuantity())
            .value(trade.getValue())
            .timestamp(trade.getTimestamp())
            .build();
    }

    
}
