package com.Exchange.core;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.Exchange.model.Order;

public class PriceLevel 
{
    private BigDecimal price;
    private Deque<Order> orders;
    private AtomicInteger totalVolume;
    private ReentrantLock lock;

    public PriceLevel(BigDecimal price)
    {
        this.price=price;
        this.orders=new ConcurrentLinkedDeque<>();
        this.totalVolume=new AtomicInteger(0);
        this.lock=new ReentrantLock();
    }

    public void addOrder(Order order)
    {
        lock.lock();
        try
        {
            orders.addLast(order);
            totalVolume.addAndGet(order.getRemainingQuantiity());
        }
        finally{
            lock.unlock();
        }
        // orders.addLast(order);
        // totalVolume+=order.getRemainingQuantiity();

    }

    public boolean removeOrder(String orderId)
    {
        lock.lock();
        try
        {
            Iterator<Order> iterator=orders.iterator();
            while(iterator.hasNext())
            {
                Order order=iterator.next();
                if(order.getOrderId().equals(orderId))
                {
                    totalVolume.getAndAdd(-order.getRemainingQuantiity());
                    iterator.remove();
                    return true;
                }
            }
            return false;
        }
        finally
        {
            lock.unlock();
        }
    }

    public Order getFirstOrder()
    {
        return orders.peekFirst();
    }

    public void removeFirstOrderIfFilled()
    {
        lock.lock();
        try
        {
            Order order=orders.peekFirst();
            if(order!=null && order.getRemainingQuantiity()==0)
            {
                orders.pollFirst();
            }
        }
        finally{
            lock.unlock();
        }
    }

    public void updateVolume(int delta)
    {
        totalVolume.addAndGet(delta);
    }

    public void reduceVolume(int quantity)
    {
        totalVolume.addAndGet(-quantity);
    }
    
    public boolean isEmpty()
    {
        return orders.isEmpty();
    }

    public int getTotalVolume()
    {
        return totalVolume.get();
    }

    public int getOrderCount()
    {
        return orders.size();
    }
    
    public BigDecimal getPrice() {
        return price;
    }

    public Deque<Order> getOrders() {
        return orders;
    }

    public void print() {
        System.out.println("PriceLevel " + price);
        for (Order o : orders) {
            System.out.println("  " + o.getOrderId() + " → " + o.getRemainingQuantiity());
        }
        System.out.println("Total Volume = " + totalVolume);
}


}
