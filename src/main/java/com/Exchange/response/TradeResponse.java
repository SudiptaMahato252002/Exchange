package com.Exchange.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeResponse 
{
    private String tradeId;
    private String buyOrderId;
    private String sellOrderId;
    private String baseAsset;
    private String quoteAsset;
    private BigDecimal price;
    private BigDecimal value;
    private int quantity;
    private long timestamp;
    
}
