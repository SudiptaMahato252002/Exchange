package com.Exchange.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.Exchange.message.TradeMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TradeConsumer 
{
    @KafkaListener(topics = "trades-ouptut",groupId ="exchnage-group-trades",containerFactory = "tradeKafkaListener" )
    public void consumeTrade(@Payload TradeMessage tradeMessage,@Header(KafkaHeaders.RECEIVED_PARTITION)int partition,@Header(KafkaHeaders.OFFSET)long offset)
    {
        log.info("Received trade from Kafka: tradeId={}, price={}, quantity={}, partition={}, offset={}", 
            tradeMessage.getTradeId(), 
            tradeMessage.getPrice(),
            tradeMessage.getQuantity(),
            partition, 
            offset);
        try 
        {
            processTrade(tradeMessage);
        } 
        catch (Exception e) 
        {
            log.error("Failed to process trade: tradeId={}, error={}", 
                tradeMessage.getTradeId(), e.getMessage(), e);
        }

    }

    private void processTrade(TradeMessage trade)
    {
        log.debug("Processing trade: {}", trade);
    }
    
}
