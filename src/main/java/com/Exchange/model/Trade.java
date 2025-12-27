package com.Exchange.model;

import java.math.BigDecimal;

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
    private BigDecimal price;
    private int quantity;
    private long timestamp;
    
}
