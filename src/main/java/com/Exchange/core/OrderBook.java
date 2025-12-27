package com.Exchange.core;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.Exchange.model.Order;

public class OrderBook 
{
    private final TreeMap<BigDecimal,PriceLevel> bids=new TreeMap<>(Comparator.reverseOrder());
    private final TreeMap<BigDecimal,PriceLevel> asks=new TreeMap<>();
    // private final Map<String,Order> orderMap;
    private BigDecimal lastTradePrice;
    
}
