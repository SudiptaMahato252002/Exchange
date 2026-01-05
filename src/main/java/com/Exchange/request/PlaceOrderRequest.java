package com.Exchange.request;

import java.math.BigDecimal;

import com.Exchange.model.OrderSide;
import com.Exchange.model.OrderType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest 
{
    private String userId;
    private String baseAsset;
    private String quoteAsset;
    private OrderSide side;
    private OrderType type;
    private BigDecimal price;
    private int quantity;

    
}
