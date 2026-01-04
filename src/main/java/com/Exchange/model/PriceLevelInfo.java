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
public class PriceLevelInfo 
{
    private BigDecimal price;
    private int totalQuantity;
    private int orderCount;   
}
