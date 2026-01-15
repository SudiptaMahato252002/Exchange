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
public class Candle 
{
    private String baseAsset;
    private String quoteAsset;
    private String interval;
    private long openTime;
    private long closeTime;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal open;
    private BigDecimal close;
    private int volume;
    private int numberOfTrades;


    public void updateWihtTrade(BigDecimal price,int quantity)
    {
        if(open==null)
        {
            high=price;
            low=price;
            open=price;
        }
        close=price;

        if(price.compareTo(high)>0)
        {
            high=price;
        }
        if(price.compareTo(low)<0)
        {
            low=price;
        }
        numberOfTrades++;
        volume+=quantity;
    }
    
    public boolean isInitialized()
    {
        return open!=null;
    }
}
