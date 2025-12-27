package com.Exchange.core;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;

import com.Exchange.model.Order;

public class PriceLevel 
{
    private BigDecimal price;
    private LinkedList<Order> orders;
    private int totalVolume;

    public PriceLevel(BigDecimal price)
    {
        this.price=price;
        this.orders=new LinkedList<>();
        this.totalVolume=0;
    }

    public void addOrder(Order order)
    {
        orders.addLast(order);
        totalVolume+=order.getRemainingQuantiity();

    }

    public boolean removeOrder(String orderId)
    {
        Iterator<Order> iterator=orders.iterator();
        while(iterator.hasNext())
        {
            Order order=iterator.next();
            if(order.getOrderId().equals(orderId))
            {
                totalVolume-=order.getRemainingQuantiity();
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public Order getFirstOrder()
    {
        return orders.peekFirst();
    }

    // public Order removeFirstOrder()
    // {

    // }

    public boolean isEmpty()
    {
        return orders.isEmpty();
    }

    public int getTotalVolume()
    {
        return totalVolume;
    }

    public int getOrderCount()
    {
        return orders.size();
    }
    
    
    public BigDecimal getPrice() {
        return price;
    }

    public LinkedList<Order> getOrders() {
        return orders;
    }
}
