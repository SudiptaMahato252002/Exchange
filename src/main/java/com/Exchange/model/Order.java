package com.Exchange.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order 
{
    private String orderId;
    private String userId;
    private OrderSide side;
    private OrderType type;
    private BigDecimal price;
    private int quantity;
    private int remainingQuantiity;
    private long timeStamp;
    private OrderStatus status;

    public Order(int quantity)
    {
        this.quantity=quantity;
        this.remainingQuantiity=quantity;
    }
    public void initRemainingQuantity() 
    {
        this.remainingQuantiity = this.quantity;
    }

}
