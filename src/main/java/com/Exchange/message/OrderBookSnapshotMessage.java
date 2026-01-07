package com.Exchange.message;

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
public class OrderBookSnapshotMessage 
{
    private List<PriceLevelInfo> bids;
    private List<PriceLevelInfo> asks;
    private BigDecimal lastTradePrice;
    private String baseAsset;
    private String quoteAsset;
    private long timeStamp;
    private int snapshotVersion;
    
    
}
