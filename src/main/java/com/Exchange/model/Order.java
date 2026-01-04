package com.Exchange.model;

import java.math.BigDecimal;
import java.util.UUID;

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
    private String baseAsset;
    private String quoteAsset;
    private OrderSide side;
    private OrderType type;
    private BigDecimal price;
    private int quantity;
    private int remainingQuantiity;
    private long timeStamp;
    private OrderStatus status;

    public static String generateOrderId(String userId)
    {
        return userId+"-"+System.nanoTime()+"-"+UUID.randomUUID().toString().substring(0, 8);
    }

    public Order(String userId,String baseAsset,String quoteAsset,OrderSide side,OrderType type,BigDecimal price,int quantity)
    {
        this.orderId=generateOrderId(userId);
        this.userId=userId;
        this.baseAsset=baseAsset;
        this.quoteAsset=quoteAsset;
        this.side=side;
        this.type=type;
        this.price=price;
        this.quantity=quantity;
        this.remainingQuantiity=quantity;
        this.timeStamp = System.currentTimeMillis();
        this.status = OrderStatus.OPEN;
    }

    public Order(int quantity)
    {
        this.quantity=quantity;
        this.remainingQuantiity=quantity;
    }
    public void initRemainingQuantity() 
    {
        this.remainingQuantiity = this.quantity;
        if (this.status == null) 
        {
            this.status = OrderStatus.OPEN;
        }
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

    public void validate()
    {
        if(price!=null && price.compareTo(BigDecimal.ZERO)<=0)
        {
            throw new IllegalArgumentException("Price must be positive");
        }
        if(quantity<=0)
        {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if(type == OrderType.LIMIT && price== null)
        {
            throw new IllegalArgumentException("Limit order must have a price");
        }
        if (baseAsset == null || baseAsset.isEmpty()) 
        {
            throw new IllegalArgumentException("Base asset is required");
        }
        if (quoteAsset == null || quoteAsset.isEmpty()) 
        {
            throw new IllegalArgumentException("Quote asset is required");
        }
    }

}
