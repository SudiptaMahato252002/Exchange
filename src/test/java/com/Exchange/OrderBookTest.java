package com.Exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.Exchange.core.OrderBook;
import com.Exchange.core.PriceLevel;
import com.Exchange.model.Order;
import com.Exchange.model.OrderSide;
import com.Exchange.model.OrderType;
import com.Exchange.model.Trade;

public class OrderBookTest 
{
    private OrderBook orderBook;

    @BeforeEach
    void setup()
    {
        orderBook=new OrderBook();
    }
    
    @Test
    void addBuyOrderToBids()
    {
        Order buy1 = Order.builder()
            .orderId("B1")
            .side(OrderSide.BUY)
            .type(OrderType.LIMIT)
            .price(BigDecimal.valueOf(100))
            .quantity(10)
            .build();

    Order buy2 = Order.builder()
            .orderId("B2")
            .side(OrderSide.BUY)
            .type(OrderType.LIMIT)
            .price(BigDecimal.valueOf(100))
            .quantity(20)
            .build();

    Order buy3 = Order.builder()
            .orderId("B3")
            .side(OrderSide.BUY)
            .type(OrderType.LIMIT)
            .price(BigDecimal.valueOf(100))
            .quantity(30)
            .build();

        Order sell = Order.builder()
                .orderId("S1")
                .side(OrderSide.SELL)
                .type(OrderType.LIMIT)
                .price(BigDecimal.valueOf(105))
                .quantity(5)
                .build();

        

        List<Trade> trades1 = orderBook.addOrder(buy1);
        List<Trade> trades2 = orderBook.addOrder(buy2);
        List<Trade> trades3 = orderBook.addOrder(buy3);
        orderBook.addOrder(sell);

        orderBook.printPretty();

        assertTrue(trades1.isEmpty());
        assertTrue(trades2.isEmpty());
        assertTrue(trades3.isEmpty());

        assertEquals(1, orderBook.getBids().size());
        assertEquals(1,orderBook.getAsks().size());

        PriceLevel level = orderBook.getBids().get(BigDecimal.valueOf(100));
        PriceLevel level2=orderBook.getAsks().get(BigDecimal.valueOf(105));
        assertEquals(3, level.getOrderCount());
        assertEquals(60, level.getTotalVolume());
        assertEquals(1, level2.getOrderCount());
        assertEquals(5, level2.getTotalVolume());
    }
}
