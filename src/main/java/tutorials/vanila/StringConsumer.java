package tutorials.vanila;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

public class StringConsumer implements Consumer<String> {

    private final Logger LOGGER = LoggerFactory.getLogger(StringMessageProducer.class);
    private final KafkaConsumer consumer;
    private final String topic;


    StringConsumer(KafkaConsumer consumer, String topic) {
        this.consumer = consumer;
        this.topic = topic;
        this.consumer.subscribe(Collections.singleton(topic));
    }

    @Override
    public void consume() {
        LOGGER.info("Consuming string message");
        while(true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.of(10000, ChronoUnit.NANOS));
            System.out.println("Record fetched = " + consumerRecords.count());
            consumerRecords
                    .forEach(record -> {
                        System.out.println("key " + record.key());
                        System.out.println("value " + record.value());
                    });

            consumer.commitSync();

            if(consumerRecords.count() > 0)
                break;
        }




//        LOGGER.info("Message consumed");
    }
}
