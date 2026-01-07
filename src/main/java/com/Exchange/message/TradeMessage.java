package com.Exchange.message;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeMessage 
{
    private String tradeId;
    private String buyOrderId;
    private String sellOrderId;
    private String baseAsset;
    private String quoteAsset;
    private BigDecimal price;
    private int quantity;
    private BigDecimal value;
    private long timestamp;
    
}
