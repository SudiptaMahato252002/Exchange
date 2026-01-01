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

    public void reduce(int quantity)
    {
        if(quantity<=0)
        {
            throw new IllegalArgumentException("Executed quantity must be positive");
        }
        if(quantity>this.remainingQuantiity)
        {
             throw new IllegalArgumentException("Executed quantity exceeds remaining quantity");
        }
        
        this.remainingQuantiity-=quantity;
        
        if (this.remainingQuantiity == 0) {
        this.status = OrderStatus.FILLED;
        } 
        else 
        {
            this.status = OrderStatus.PARTIALLY_FILLED;
        }
    }

}
