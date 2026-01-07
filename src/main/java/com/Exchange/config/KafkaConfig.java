package com.Exchange.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.Exchange.message.OrderMessage;
import com.Exchange.message.TradeMessage;



@Configuration
@EnableKafka
public class KafkaConfig 
{

    @Value("${spring.kafka.bootstrap-servers:localhost:29092}")
    private String bootStrapServers;

    @Value("${spring.kafka.consumer.group-id:exchange-group}")
    private String groupId;

    @Bean
    public ProducerFactory<String,OrderMessage> orderProducerFactory()
    {

        Map<String,Object> config=new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootStrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG,"all");
        config.put(ProducerConfig.RETRIES_CONFIG,3);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,true);

        return new DefaultKafkaProducerFactory<>(config);


    }

    @Bean
    public KafkaTemplate<String,OrderMessage> orderKafkaTemplate()
    {
        return new KafkaTemplate<>(orderProducerFactory());
    }


    @Bean
    public ProducerFactory<String,TradeMessage> tradeProducerFactory()
    {
        Map<String,Object> config=new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,JsonSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG,"all");
        // config.put(ProducerConfig.RETRIES_CONFIG, 3);
        // config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,true);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, TradeMessage> tradeKafkaTemplate() 
    {
        return new KafkaTemplate<>(tradeProducerFactory());
    }

    @Bean
    public ProducerFactory<String, String> stringProducerFactory() 
    {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate() 
    {
        return new KafkaTemplate<>(stringProducerFactory());
    }

    @Bean
    public ConsumerFactory<String,OrderMessage> orderConsumerFactory()
    {
        Map<String,Object> config=new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,JsonDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        config.put(JsonDeserializer.TRUSTED_PACKAGES,"com.Exchange.message");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE,OrderMessage.class.getName());

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,OrderMessage> orderKafkaListener()
    {
        ConcurrentKafkaListenerContainerFactory<String,OrderMessage> factory=new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
        factory.setConcurrency(3);
        return factory;
    }

    @Bean
    public ConsumerFactory<String,TradeMessage> tradeConsumerFactory()
    {
        Map<String,Object> config=new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,JsonDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        config.put(JsonDeserializer.TRUSTED_PACKAGES,"com.Exchange.message");
        config.put(JsonDeserializer.VALUE_DEFAULT_TYPE,TradeMessage.class.getName());

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,TradeMessage> tradeKafkaListener()
    {
        ConcurrentKafkaListenerContainerFactory<String,TradeMessage> factory=new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(tradeConsumerFactory());
        factory.setConcurrency(3);
        return factory;
    }

    
}
