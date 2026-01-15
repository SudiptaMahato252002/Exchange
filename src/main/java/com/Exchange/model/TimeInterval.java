package com.Exchange.model;

import lombok.Getter;

@Getter
public enum TimeInterval 
{
    ONE_MINUTE("1m",60*1000L),
    FIVE_MINUTES("5m",5*60*1000l),
    FIFTEEN_MINUTES("15m", 15 * 60 * 1000L),
    THIRTY_MINUTES("30m", 30 * 60 * 1000L),
    ONE_HOUR("1h", 60 * 60 * 1000L),
    ONE_DAY("1d", 24 * 60 * 60 * 1000L),
    THIRTY_DAYS("30d",30*24*60*60*1000l);

    private final String code;
    private final long miliSeconds;

    TimeInterval(String code,long miliSeconds)
    {
        this.code=code;
        this.miliSeconds=miliSeconds;
    }

    public static TimeInterval getCode(String code)
    {
        for(TimeInterval interval:values())
        {
            if(interval.code.equals(code))
            {
                return interval;
            }
        }
        throw new IllegalArgumentException("Invalid interval code: " + code);

    }

    public long getStartTime(long timestamp)
    {
        return (timestamp/miliSeconds)*miliSeconds;

    }

    public long getEndTime(long timestamp)
    {
        return getStartTime(timestamp)+miliSeconds-1;
    }

    
}
