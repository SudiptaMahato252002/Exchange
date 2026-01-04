package com.Exchange.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.Exchange.model.Order;
import com.Exchange.model.OrderBookSnapShot;
import com.Exchange.model.Trade;

public class OrderBookManager 
{

    private final Map<String,OrderBook> orderBooks=new ConcurrentHashMap<>();
    

    public OrderBook getOrCreateOrderBook(String baseAsset,String quoteAsset)
    {
        String symbol=getSymbol(baseAsset, quoteAsset);
        return orderBooks.computeIfAbsent(symbol, k->new OrderBook(baseAsset,quoteAsset));
    }

    public List<Trade> addOrder(Order order)
    {
        OrderBook orderBook=getOrCreateOrderBook(order.getBaseAsset(), order.getQuoteAsset());
        return orderBook.addOrder(order);
    }


    public boolean cancelOrder(String baseAsset,String quoteAsset,String orderId)
    {
        String symbol=getSymbol(baseAsset, quoteAsset);
        OrderBook orderBook=orderBooks.get(symbol);
        return orderBook.cancelOrder(orderId);
    }

    public Optional<Order> getOrder(String baseAsset,String quoteAsset,String orderId)
    {
        String symbol=getSymbol(baseAsset, quoteAsset);        
        OrderBook orderBook=orderBooks.get(symbol);
        return orderBook!=null?orderBook.getOrder(orderId):Optional.empty();
    }
    public OrderBookSnapShot getSnapShot(String baseAsset,String quoteAsset,int depth)
    {
        OrderBook orderBook=getOrCreateOrderBook(baseAsset, quoteAsset);
        return orderBook.getSnapShot(depth);

    }

    public List<String> getAvailableSymbols() 
    {
        return List.copyOf(orderBooks.keySet());
    }

    private String getSymbol(String baseAsset,String qutoeAsset)
    {
        return baseAsset+"/"+qutoeAsset;
    }
}
