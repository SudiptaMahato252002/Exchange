package com.Exchange.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.Exchange.core.OrderBook;
import com.Exchange.model.Order;
import com.Exchange.model.Trade;
import com.Exchange.request.CancelOrderRequest;
import com.Exchange.request.PlaceOrderRequest;
import com.Exchange.response.OrderResponse;
import com.Exchange.response.TradeResponse;
import com.Exchange.websocket.WebSocketMessageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService 
{
    private final OrderBookService orderBookService;
    private final TradeService tradeService;
    private final WebSocketMessageService webSocketService;

    public OrderResponse placeOrder(PlaceOrderRequest request)
    {
        Order order=Order.builder()
            .orderId(Order.generateOrderId(request.getUserId()))
            .userId(request.getUserId())
            .baseAsset(request.getBaseAsset())
            .quoteAsset(request.getQuoteAsset())
            .side(request.getSide())
            .type(request.getType())
            .price(request.getPrice())
            .quantity(request.getQuantity())
            .timeStamp(System.currentTimeMillis())
        .build();

        OrderBook orderBook=orderBookService.getOrderBook(request.getBaseAsset(), request.getQuoteAsset());
        List<Trade> trades=orderBook.addOrder(order);
        tradeService.recordTrades(trades);
        trades.forEach(webSocketService::broadcastTrade);

        var orderBookSnapshot = orderBookService.getOrderBookSnapshot(
            request.getBaseAsset(), 
            request.getQuoteAsset(), 
            10
        );
        webSocketService.orderBookUpdate(orderBookSnapshot);

        return convertToResponse(order, trades);
    }

    public boolean cancelOrder(CancelOrderRequest request)
    {
        OrderBook orderBook=orderBookService.getOrderBook(request.getBaseAsset(), request.getQuoteAsset());
        boolean cancelled=orderBook.cancelOrder(request.getOrderId());
        
        if(cancelled)
        {
            var OrderBookSnapShot=orderBookService.getOrderBookSnapshot(request.getBaseAsset(),request.getQuoteAsset(), 10);
            webSocketService.orderBookUpdate(OrderBookSnapShot);        
        }
        return cancelled;
    }

    public Optional<OrderResponse> getOrder(String baseAsset,String quoteAsset,String orderId)
    {
        OrderBook orderBook=orderBookService.getOrderBook(baseAsset, quoteAsset);
        Optional<Order> order=orderBook.getOrder(orderId);
        return order.map(o->convertToResponse(o, List.of()));
    }

    public List<OrderResponse> getuserOrders(String baseAsset,String quoteAsset,String userId)
    {   
        OrderBook orderBook=orderBookService.getOrderBook(baseAsset, quoteAsset);
        List<Order> orders=orderBook.getUserOrders(userId);

        return orders.stream()
            .map(order->convertToResponse(order, List.of()))
            .collect(Collectors.toList());
    }

    private OrderResponse convertToResponse(Order order,List<Trade> trades)
    {
        List<TradeResponse> tradeResponse=trades.stream().map(this::convertToTradeResponse).collect(Collectors.toList());
        return OrderResponse.builder()
            .orderId(order.getOrderId())
            .userId(order.getUserId())
            .baseAsset(order.getBaseAsset())
            .quoteAsset(order.getQuoteAsset())
            .side(order.getSide())
            .type(order.getType())
            .price(order.getPrice())
            .quantity(order.getQuantity())
            .remainingQuantiity(order.getRemainingQuantiity())
            .timeStamp(order.getTimeStamp())
            .status(order.getStatus())
            .trades(tradeResponse)
        .build();

    }

     private TradeResponse convertToTradeResponse(Trade trade) 
    {
        return TradeResponse.builder()
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
