package com.Exchange.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookSnapShot 
{
    private List<PriceLevelInfo> bids;
    private List<PriceLevelInfo> asks;
    private BigDecimal lastTradePrice;
    
}

class PriceLevelInfo
{
    private BigDecimal price;
    private int totalQuantity;
    private int orderCount;
}
