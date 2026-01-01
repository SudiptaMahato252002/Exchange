package com.Exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

public class MatchingEngineTest 
{
    private OrderBook orderBook;

    @BeforeEach
    void setup()
    {
        orderBook=new OrderBook();
    }
    
    // @Test
    // void shouldFullyMatchBuyAndSellOrder()
    // {
    //     Order sell1 = Order.builder()
    //         .orderId("S1")
    //         .side(OrderSide.SELL)
    //         .type(OrderType.LIMIT)
    //         .price(BigDecimal.valueOf(105)) // NOT matchable
    //         .quantity(10)
    //         .build();

    //     Order sell2 = Order.builder()
    //             .orderId("S2")
    //             .side(OrderSide.SELL)
    //             .type(OrderType.LIMIT)
    //             .price(BigDecimal.valueOf(100)) // MATCHABLE
    //             .quantity(5)
    //             .build();

    //     Order sell3 = Order.builder()
    //             .orderId("S3")
    //             .side(OrderSide.SELL)
    //             .type(OrderType.LIMIT)
    //             .price(BigDecimal.valueOf(100))
    //             .quantity(5)
    //             .build();

    //     Order buy1 = Order.builder()
    //             .orderId("B1")
    //             .side(OrderSide.BUY)
    //             .type(OrderType.LIMIT)
    //             .price(BigDecimal.valueOf(100))
    //             .quantity(5)
    //             .build();

    //     Order buy2 = Order.builder()
    //             .orderId("B2")
    //             .side(OrderSide.BUY)
    //             .type(OrderType.LIMIT)
    //             .price(BigDecimal.valueOf(100))
    //             .quantity(5)
    //             .build();

    //     Order buy3 = Order.builder()
    //             .orderId("B3")
    //             .side(OrderSide.BUY)
    //             .type(OrderType.LIMIT)
    //             .price(BigDecimal.valueOf(100))
    //             .quantity(5)
    //             .build();

    //     orderBook.addOrder(sell1);
    //     orderBook.addOrder(sell2);
    //     orderBook.addOrder(sell3);

    //     System.out.println("📕 ORDER BOOK BEFORE FIRST BUY");
    //     orderBook.printPretty();

    //     List<Trade> trades=orderBook.addOrder(buy1);

    //     System.out.println("📕 ORDER BOOK AFTER FIRST BUY");
    //     orderBook.printPretty();

    //     assertEquals(1, trades.size());
    //     Trade trade = trades.get(0);
    //     System.out.println(trade);
    //     System.out.println(trade.getBuyOrderId());
    //     System.out.println(trade.getSellOrderId());
    //     assertEquals("S2", trade.getSellOrderId());
    //     assertEquals("B1", trade.getBuyOrderId());
    //     assertEquals(5, trade.getQuantity());
    //     assertEquals(BigDecimal.valueOf(100), trade.getPrice());

    // }

    @Test
    void shouldPartiallyFillOrder() {

        Order sell = Order.builder()
                .orderId("S1")
                .side(OrderSide.SELL)
                .type(OrderType.LIMIT)
                .price(BigDecimal.valueOf(100))
                .quantity(5)
                .build();

        Order buy = Order.builder()
                .orderId("B1")
                .side(OrderSide.BUY)
                .type(OrderType.LIMIT)
                .price(BigDecimal.valueOf(100))
                .quantity(10)
                .build();

        // Add SELL first → goes to book
        orderBook.addOrder(sell);
        orderBook.printPretty();

        // Add BUY → partially matches
        List<Trade> trades = orderBook.addOrder(buy);

        // 🔹 Trade assertions
        assertEquals(1, trades.size());

        Trade trade = trades.get(0);
        System.out.println(trade);
        assertEquals("B1", trade.getBuyOrderId());
        assertEquals("S1", trade.getSellOrderId());
        assertEquals(5, trade.getQuantity());
        assertEquals(BigDecimal.valueOf(100), trade.getPrice());

        // 🔹 Remaining quantity on BUY
        assertEquals(5, buy.getRemainingQuantiity());

        // 🔹 SELL side should be empty (fully filled)
        assertTrue(orderBook.getAsks().isEmpty());

        // 🔹 BUY side should contain remaining quantity
        assertEquals(1, orderBook.getBids().size());

        orderBook.printPretty();
        PriceLevel bidLevel = orderBook.getBids().get(BigDecimal.valueOf(100));
        System.out.println(bidLevel.getTotalVolume());
        assertNotNull(bidLevel);
        assertEquals(5, bidLevel.getTotalVolume());
        assertEquals(1, bidLevel.getOrderCount());
}


}
