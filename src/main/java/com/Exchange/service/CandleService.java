package com.Exchange.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.Exchange.core.CandleStore;
import com.Exchange.model.Candle;
import com.Exchange.model.TimeInterval;
import com.Exchange.model.Trade;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CandleService 
{
    private final Map<String,Map<TimeInterval,CandleStore>> store=new ConcurrentHashMap<>();
    
    public void processTrade(Trade trade)
    {
        log.info("Processing trade | symbol={} | price={} | qty={} | ts={}",
            getSymbols(trade.getBaseAsset(), trade.getQuoteAsset()),
            trade.getPrice(),
            trade.getQuantity(),
            trade.getTimestamp());
        String symbol=getSymbols(trade.getBaseAsset(), trade.getQuoteAsset());
        for(TimeInterval interval:TimeInterval.values())
        {
            updateCandle(symbol, interval, trade);
        }
    }

    private void updateCandle(String symbol,TimeInterval interval,Trade trade)
    {
        CandleStore candleStore=store.computeIfAbsent(symbol, s->new ConcurrentHashMap<>()).computeIfAbsent(interval, i->new CandleStore());
        long openTime=interval.getStartTime(trade.getTimestamp());
        long closeTime=interval.getEndTime(trade.getTimestamp());

        Candle candle=candleStore.getOrCreate(()->
            Candle.builder()
                .baseAsset(trade.getBaseAsset())
                .quoteAsset(trade.getQuoteAsset())
                .openTime(openTime)
                .closeTime(closeTime)
                .interval(interval.getCode())
            .build()
        , openTime);

        synchronized(candle)
        {
            candle.updateWihtTrade(trade.getPrice(), trade.getQuantity());
        }
    }

   public List<Candle> getCandles(String interval,String base,String quote,int limit)
   {
        CandleStore candleStore=getStore(base, quote, interval);
        if(candleStore==null)
        {
            return List.of();
        }
        List<Candle> all=candleStore.snapshot();
        return all.subList(Math.max(0,all.size()-limit), all.size());
    }   

    public List<Candle> getCandlesInRange(String base,String quote,String interval,long start,long end)
    {
        CandleStore candleStore=getStore(base, quote, interval);
        if(candleStore==null)
        {
            return List.of();
        }

       return candleStore.snapshot().stream().filter(c->c.getOpenTime()>=start && c.getOpenTime()<=end).toList();
    }

   private CandleStore getStore(String base,String quote,String intervalCode)
   {
        String symbol=getSymbols(base, quote);
        Map<TimeInterval,CandleStore> symbolMap=store.get(symbol);
        if(symbolMap==null)
        {
            return null;
        }
        return symbolMap.get(TimeInterval.getCode(intervalCode));
   }
    
    private String getSymbols(String baseAsset,String quoteAsset)
    {
        return baseAsset+"/"+quoteAsset;
    }
    
    
}
