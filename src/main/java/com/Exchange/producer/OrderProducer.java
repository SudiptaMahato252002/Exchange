package com.Exchange.producer;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import com.Exchange.message.OrderMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderProducer 
{
    @Autowired
    private KafkaTemplate<String,OrderMessage> kafkaTemplate;
    
    private static final String TOPIC="orders-input";

    public void sendOrder(OrderMessage order)
    {
        try 
        {
            CompletableFuture<SendResult<String,OrderMessage>> future=kafkaTemplate.send(TOPIC,order.getOrderId(),order);
            future.whenComplete((result,ex)->{
                if(ex==null)
                {
                    log.info("Order sent successfully: orderId={}, offset={}", 
                        order.getOrderId(), 
                        result.getRecordMetadata().offset());
                }
                else
                {
                    log.error("Failed to send order: orderId={}, error={}", 
                        order.getOrderId(), 
                        ex.getMessage());
                }
            });
        } 
        catch (Exception e) 
        {
            log.error("Exception while sending order to Kafka: {}", e.getMessage(), e);
        }
    }

    public void sendOrderSync(OrderMessage order) throws Exception
    {
        try 
        {
            SendResult<String,OrderMessage> result=kafkaTemplate.send(TOPIC,order.getOrderId(),order).get();
            
            log.info("Order sent successfully (sync): orderId={}, offset={}", 
                order.getOrderId(), 
                result.getRecordMetadata().offset());

        } 
        catch (Exception e) 
        {
            log.error("Failed to send order (sync): orderId={}, error={}", 
                order.getOrderId(), 
                e.getMessage());
            throw e;
        }
    }
    
}
