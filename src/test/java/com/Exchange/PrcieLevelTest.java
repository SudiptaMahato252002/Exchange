package com.Exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.Exchange.core.PriceLevel;
import com.Exchange.model.Order;

public class PrcieLevelTest 
{

    // private PriceLevel priceLevel;
    // @BeforeEach
    // void setup()
    // {
    //     priceLevel=new PriceLevel(BigDecimal.valueOf(100));
    // }

    // @Test
    // void shouldAddMultipleOrders()
    // {
    //     Order order1=Order.builder()
    //         .orderId("O1")
    //         .quantity(10)
    //         .remainingQuantiity(10)
    //         .build();

    //     Order order2=Order.builder()
    //             .orderId("O2")
    //             .quantity(20)
    //             .remainingQuantiity(20)
    //             .build();

    //     Order order3=Order.builder()
    //             .orderId("O3")
    //             .quantity(30)
    //             .remainingQuantiity(30)
    //             .build();

    //     priceLevel.addOrder(order1);
    //     priceLevel.addOrder(order2);
    //     priceLevel.addOrder(order3);

    //     priceLevel.print();

    //     assertEquals(3,priceLevel.getOrderCount());
    //     assertEquals(60, priceLevel.getTotalVolume());

    // }

}
