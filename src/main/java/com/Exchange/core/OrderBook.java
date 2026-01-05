package com.Exchange.core;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import com.Exchange.model.Order;
import com.Exchange.model.OrderBookSnapShot;
import com.Exchange.model.OrderSide;
import com.Exchange.model.OrderStatus;
import com.Exchange.model.PriceLevelInfo;
import com.Exchange.model.Trade;

import lombok.Data;

@Data
public class OrderBook 
{
    private final ConcurrentSkipListMap<BigDecimal,PriceLevel> bids=new ConcurrentSkipListMap<>((a,b)->b.compareTo(a));
    private final ConcurrentSkipListMap<BigDecimal,PriceLevel> asks=new ConcurrentSkipListMap<>();
    private final ConcurrentHashMap<String,Order> orderBook=new ConcurrentHashMap<>();
    private BigDecimal lastTradePrice;
    private final String baseAsset;
    private final String quoteAsset;

    public OrderBook(String baseAsset,String quoteAsset)
    {
        this.baseAsset=baseAsset;
        this.quoteAsset=quoteAsset;
    }
     public OrderBook() 
    {
        this("DEFAULT", "DEFAULT");
    }

    

    MatchingEngine engine=new MatchingEngine();

    public List<Trade> addOrder(Order order)
    {
        order.validate();

         if (!order.getBaseAsset().equals(baseAsset) || 
            !order.getQuoteAsset().equals(quoteAsset)) 
        {
            throw new IllegalArgumentException(
                String.format("Order trading pair %s/%s does not match orderbook %s/%s",
                    order.getBaseAsset(), order.getQuoteAsset(), baseAsset, quoteAsset)
            );
        }

        order.initRemainingQuantity();
        List<Trade> trades=engine.matchOrder(order,this);

        if(!trades.isEmpty())
        {
            lastTradePrice=trades.get(trades.size()-1).getPrice();
        }
        if(order.getRemainingQuantiity()>0)
        {
            addToBook(order);
            orderBook.put(order.getOrderId(), order);
        }
        return trades;
    }
    
    public void addToBook(Order order)
    {   
        ConcurrentSkipListMap<BigDecimal,PriceLevel> bookSide=order.getSide()==OrderSide.BUY?bids:asks;
        PriceLevel level=bookSide.computeIfAbsent(order.getPrice(),PriceLevel::new);
        level.addOrder(order);

    }
    
    public boolean cancelOrder(String orderId)
    {
        Order order=orderBook.get(orderId);
        if (order == null) 
        {
            return false;
        }
        ConcurrentSkipListMap<BigDecimal,PriceLevel> bookLevel=order.getSide()==OrderSide.BUY?bids:asks;
        PriceLevel level=bookLevel.get(order.getPrice());
        if(bookLevel!=null)
        {
            boolean removed=level.removeOrder(orderId);
            if(removed)
            {
                order.setStatus(OrderStatus.CANCELLED);
                if (level.isEmpty()) {
                    bookLevel.remove(order.getPrice());
                }
                return true;
            }
        }
        return false;
    }

    public Optional<Order> getOrder(String orderId)
    {
        return Optional.ofNullable(orderBook.get(orderId));
    }

    public List<Order> getUserOrders(String userId)
    {
        return orderBook.values().stream().filter(order->order.getUserId().equals(userId)).collect(Collectors.toList());
    }

    public OrderBookSnapShot getSnapShot(int depth)
    {
        List<PriceLevelInfo> bidsLevels=bids.entrySet().stream()
                                        .limit(depth)
                                        .map(entry->PriceLevelInfo.builder()
                                                        .price(entry.getKey())
                                                        .totalQuantity(entry.getValue().getTotalVolume())
                                                        .orderCount(entry.getValue().getOrderCount())
                                                        .build())
                                        .collect(Collectors.toList());
        List<PriceLevelInfo> askLevels=asks.entrySet().stream()
                                        .limit(depth)
                                        .map(entry->PriceLevelInfo.builder()
                                            .price(entry.getKey())
                                            .totalQuantity(entry.getValue().getTotalVolume())
                                            .orderCount(entry.getValue().getOrderCount())
                                            .build())
                                        .collect(Collectors.toList());
        return OrderBookSnapShot.builder()
                .bids(bidsLevels)
                .asks(askLevels)
                .lastTradePrice(lastTradePrice)
                .baseAsset(baseAsset)
                .quoteAsset(quoteAsset)
                .timeStamp(System.currentTimeMillis())
                .build();

    }

    public int getBidDepth()
    {
        return bids.size();
    }

    public int getAskDepth()
    {
        return asks.size();
    }

    public int getTotalOrders()
    {
        return orderBook.size();
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
