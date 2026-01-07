package com.Exchange.message;

import java.math.BigDecimal;

import com.Exchange.model.OrderSide;
import com.Exchange.model.OrderStatus;
import com.Exchange.model.OrderType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage 
{
    private String orderId;
    private String userId;
    private String baseAsset;
    private String quoteAsset;
    private OrderSide side;
    private OrderType type;
    private BigDecimal price;
    private int quantity;
    private long timeStamp;
    private OrderStatus status;
    
}
