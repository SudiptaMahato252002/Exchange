package com.Exchange.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.Exchange.message.OrderBookSnapshotMessage;
import com.Exchange.model.OrderBookSnapShot;
import com.Exchange.service.OrderBookService;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Component
public class OrderBookSnapshotProducer 
{
    
    @Autowired
    private KafkaTemplate<String,String> stringKafkaTemplate;
    @Autowired
    private OrderBookService orderBookService;
    
    private static final String TOPIC = "orderbook-snapshots";
    private int snapshotVersion = 0;

    @Scheduled(fixedRate = 5000)
    public void sendPeriodicSnapshots()
    {
        orderBookService.getAllSymbols().forEach(s->{
            String parts[]=s.split("/");
            if(parts.length==2)
            {
                sendSnapshot(parts[0], parts[1]);
            }
        });

    }


    public void sendSnapshot(String baseAsset,String quoteAsset)
    {
        try 
        {
            OrderBookSnapShot snapshot=orderBookService.getOrderBook(baseAsset, quoteAsset).getSnapShot(20);
            OrderBookSnapshotMessage message=OrderBookSnapshotMessage.builder()
                .bids(snapshot.getBids())
                .asks(snapshot.getAsks())
                .lastTradePrice(snapshot.getLastTradePrice())
                .baseAsset(snapshot.getBaseAsset())
                .quoteAsset(snapshot.getQuoteAsset())
                .timeStamp(snapshot.getTimeStamp())
                .snapshotVersion(++snapshotVersion)
                .build();
            String key = baseAsset + "/" + quoteAsset;
            stringKafkaTemplate.send(TOPIC,key,message.toString());
            log.debug("Snapshot sent: {}/{}, version={}", baseAsset, quoteAsset, snapshotVersion);
                
        } 
        catch (Exception e) 
        {
            log.error("Failed to send snapshot for {}/{}: {}", 
                baseAsset, quoteAsset, e.getMessage());
            // TODO: handle exception
        }
    }
}
