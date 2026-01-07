package com.Exchange.producer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.Exchange.message.TradeMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TradeProducer 
{
    @Autowired
    private KafkaTemplate<String,TradeMessage> kafkaTemplate;
    private static final String TOPIC="trades-ouptut";

    public void sendTrade(TradeMessage trade)
    {
        try 
        {
            CompletableFuture<SendResult<String,TradeMessage>> future=kafkaTemplate.send(TOPIC,trade.getTradeId(),trade);
            future.whenComplete((result,ex)->{
                if(ex==null)
                {
                    log.info("Trade sent successfully: tradeId={}, offset={}", 
                        trade.getTradeId(), 
                        result.getRecordMetadata().offset());
                }
                else
                {
                     log.error("Failed to send trade: tradeId={}, error={}", 
                        trade.getTradeId(), 
                        ex.getMessage());

                }
            });
        } 
        catch (Exception e) 
        {
             log.error("Exception while sending trade to Kafka: {}", e.getMessage(), e);
        }
    }

    public void sendTrades(List<TradeMessage> trades)
    {
        trades.forEach(this::sendTrade);
    }
}
