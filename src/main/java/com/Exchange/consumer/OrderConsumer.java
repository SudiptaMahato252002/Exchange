package com.Exchange.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.Exchange.message.OrderMessage;
import com.Exchange.service.KafkaOrderProcessingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderConsumer 
{
    @Autowired
    private KafkaOrderProcessingService orderProcessingService;
    

    @KafkaListener(topics = "order-inputs",groupId = "${spring.kafka.consumer.group-id}",containerFactory = "orderKafkaListener")
    public void consumeOrder(@Payload OrderMessage orderMessage,@Header(KafkaHeaders.RECEIVED_PARTITION)int partition,@Header(KafkaHeaders.OFFSET)long offset)
    {
        log.info("Received order from Kafka: orderId={}, partition={}, offset={}", 
            orderMessage.getOrderId(), partition, offset);
        try 
        {
            orderProcessingService.processOrder(orderMessage);
            log.info("Order processed successfully: orderId={}", orderMessage.getOrderId());
        } 
        catch (Exception e) 
        {
            log.error("Failed to process order: orderId={}, error={}", 
                orderMessage.getOrderId(), e.getMessage(), e);
            // TODO: handle exception
        }
    }


}
