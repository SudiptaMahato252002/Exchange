package com.Exchange.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trade 
{
    private String tradeId;
    private String buyOrderId;
    private String sellOrderId;
    private String baseAsset;
    private String quoteAsset;
    private BigDecimal price;
    private int quantity;
    private long timestamp;

    public static String generateTradeId()
    {
        return "T-"+System.nanoTime()+"-"+UUID.randomUUID().toString().substring(0,8);
    }

    public BigDecimal getValue()
    {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
    
}
