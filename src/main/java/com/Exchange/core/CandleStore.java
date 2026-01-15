package com.Exchange.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

import com.Exchange.model.Candle;

public class CandleStore 
{
    private static final int MAX_CANDLES=1000;
    Deque<Candle> candles=new ArrayDeque<>(MAX_CANDLES);
    private Object lock=new Object();


    public Candle getOrCreate(Supplier<Candle> factory,long openTime)
    {
        synchronized(lock)
        {
            Candle last=candles.peekLast();

            if(last!=null && last.getOpenTime()==openTime)
            {
                return last;
            }
            Candle candle=factory.get();
            candles.addLast(candle);

            if(candles.size()>MAX_CANDLES)
            {
                candles.removeFirst();
            }
            return candle;
        }
    }

    public List<Candle> snapshot()
    {
        synchronized(lock)
        {
            return new ArrayList<>(candles);
        }
    }

    public Candle latest()
    {
        synchronized(lock)
        {
            return candles.peekLast();
        }
    }
    public int size()
    {
        synchronized(lock)
        {
            return candles.size();
        }
    }
    
}
