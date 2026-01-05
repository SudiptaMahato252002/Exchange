package com.Exchange.response;

import java.math.BigDecimal;
import java.util.List;

import com.Exchange.model.PriceLevelInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookResponse 
{
    private String baseAsset;
    private String quoteAsset;
    private List<PriceLevelInfo> bids;
    private List<PriceLevelInfo> asks;
    private BigDecimal lastTradedPrice;
    private long timeStamp;
    private int totalBidsVolume;
    private int totalAsksVolume;
    private int bidsDepth;
    private int asksDepth;
}
