package com.Exchange.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.Exchange.model.Order;
import com.Exchange.model.OrderSide;
import com.Exchange.model.OrderType;
import com.Exchange.model.Trade;

public class MatchingEngine 
{

    public List<Trade> matchOrder(Order order,OrderBook orderBook)
    {
        List<Trade> trades=new ArrayList<>();
        if(order.getSide()==OrderSide.BUY)
        {
            matchBuy(order, orderBook, trades);
        }
        else
        {
            matchSell(order, orderBook, trades);
        }
        return trades;
    }
    
    private void matchBuy(Order buy,OrderBook orderBook,List<Trade> trades)
    {
            while(buy.getRemainingQuantiity()>0&&!orderBook.getAsks().isEmpty())
            {
                Map.Entry<BigDecimal,PriceLevel> bestAsk=orderBook.getAsks().firstEntry();
                BigDecimal askPrice=bestAsk.getKey();
                
                if((buy.getType()==OrderType.LIMIT)&&(askPrice.compareTo(buy.getPrice())>0))
                {
                    break;
                }
                PriceLevel level=bestAsk.getValue();
                Order sell=level.getFirstOrder();

                Trade trade=executeMatch(buy, sell, askPrice);
                trades.add(trade);

                if(sell.getRemainingQuantiity()==0)
                {
                    level.removeOrder(sell.getOrderId());
                }
                if(level.isEmpty())
                {
                    orderBook.getAsks().remove(askPrice);
                }

            }
    }
    
    private void matchSell(Order sell,OrderBook orderBook,List<Trade> trades)
    {
        while(sell.getRemainingQuantiity()>0&&!orderBook.getBids().isEmpty())
        {
            Map.Entry<BigDecimal,PriceLevel> bestBids=orderBook.getBids().firstEntry();
            BigDecimal bidPrice=bestBids.getKey();
            if(sell.getSide()==OrderSide.SELL&&sell.getPrice().compareTo(bidPrice)>0)
            {
                break;
            }
            PriceLevel level=bestBids.getValue();
            Order buy=level.getFirstOrder();

            Trade trade=executeMatch(sell, buy, bidPrice);
            trades.add(trade);

            if(buy.getRemainingQuantiity()==0)
            {
                level.removeOrder(buy.getOrderId());
            }
            if(level.isEmpty())
            {
                orderBook.getBids().remove(bidPrice);
            }
        }

    }

    private Trade executeMatch(Order incoming,Order resting,BigDecimal price)
    {
        int qty=Math.min(incoming.getRemainingQuantiity(),resting.getRemainingQuantiity());
        incoming.reduce(qty);
        resting.reduce(qty);

        return Trade.builder()
            .buyOrderId(incoming.getSide()==OrderSide.BUY?incoming.getOrderId():resting.getOrderId())
            .sellOrderId(incoming.getSide()==OrderSide.SELL?incoming.getOrderId():resting.getOrderId())
            .price(price)
            .quantity(qty)
            .timestamp(System.currentTimeMillis())
        .build();
    }
    
}
