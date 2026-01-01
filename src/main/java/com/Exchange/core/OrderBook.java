package com.Exchange.core;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import com.Exchange.model.Order;
import com.Exchange.model.OrderSide;
import com.Exchange.model.Trade;

import lombok.Data;

@Data
public class OrderBook 
{
    private final TreeMap<BigDecimal,PriceLevel> bids=new TreeMap<>(Comparator.reverseOrder());
    private final TreeMap<BigDecimal,PriceLevel> asks=new TreeMap<>();
    // private final Map<String,Order> orderMap;
    private BigDecimal lastTradePrice;
    

    MatchingEngine engine=new MatchingEngine();

    public List<Trade> addOrder(Order order)
    {
        order.initRemainingQuantity();
        List<Trade> trades=engine.matchOrder(order,this);
        if(order.getRemainingQuantiity()>0)
        {
            addToBook(order);
        }
        return trades;
    }
    
    public void addToBook(Order order)
    {   
        TreeMap<BigDecimal,PriceLevel> bookSide=order.getSide()==OrderSide.BUY?bids:asks;
        PriceLevel level=bookSide.computeIfAbsent(order.getPrice(),PriceLevel::new);
        level.addOrder(order);

    }
    public void printPretty() 
    {
        System.out.println("\n========= ORDER BOOK =========");

        System.out.println("\n----- ASKS (SELL) -----");
        if (asks.isEmpty()) {
            System.out.println("  <empty>");
        } else {
            asks.forEach((price, level) -> {
                System.out.printf("  %8s | %3d orders | total=%d | ",
                        price,
                        level.getOrderCount(),
                        level.getTotalVolume()
                );
                printOrderIds(level);
            });
        }

        System.out.println("\n----- BIDS (BUY) -----");
        if (bids.isEmpty()) 
        {
            System.out.println("  <empty>");
        } 
        else 
        {
            bids.forEach((price, level) -> {
                System.out.printf("  %8s | %3d orders | total=%d | ",
                        price,
                        level.getOrderCount(),
                        level.getTotalVolume()
                );
                printOrderIds(level);
            });
        }

        System.out.println("\n===============================\n");
    }

    private void printOrderIds(PriceLevel level) 
    {
        System.out.print("[ ");
        level.getOrders().forEach(o ->
                System.out.print(o.getOrderId() + ":" + o.getRemainingQuantiity() + " ")
        );
        System.out.println("]");
    }

}
