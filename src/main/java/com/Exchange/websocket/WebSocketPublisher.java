package com.Exchange.websocket;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.Exchange.response.OrderBookResponse;
import com.Exchange.response.TradeResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketPublisher 
{
    private final SimpMessagingTemplate messagingTemplate;

    public void publishTrade(String baseAsset,String quoteAsset,TradeResponse trade)
    {
        String topic=String.format("/topic/trades/%s/%s",baseAsset,quoteAsset);
        messagingTemplate.convertAndSend(topic,trade);
    }

    public void publishTrades(String baseAsset,String quoteAsset,List<TradeResponse> trades)
    {
        String topic=String.format("/topic/trades/%s/%s",baseAsset,quoteAsset);
        trades.forEach(trade->messagingTemplate.convertAndSend(topic,trade));
    }

    public void publishOrderBookUpdate(String baseAsset,String quoteAsset,OrderBookResponse response)
    {
        String topic=String.format("/topic/orderbook/%s/%s",baseAsset,quoteAsset);
        messagingTemplate.convertAndSend(topic,response);
    }

    public void publishOrderStatus(String userId,Object orderStatus)
    {
        String topic=String.format("/queue/orders/%s",userId);
        messagingTemplate.convertAndSend(topic,orderStatus);

    }

}
