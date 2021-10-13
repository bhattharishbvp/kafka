package tutorials.vanila;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class StringMessageProducer implements Producer<String> {

    private final Logger LOGGER = LoggerFactory.getLogger(StringMessageProducer.class);
    private final String topic;
    private final KafkaProducer kafkaProducer;

    public StringMessageProducer(String topic, KafkaProducer kafkaProducer) {
        this.topic = topic;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void produce(String message) throws ExecutionException, InterruptedException {
        LOGGER.info("Producing string message");
        ProducerRecord producerRecord = new ProducerRecord(topic, message);
        Future<RecordMetadata> future = kafkaProducer.send(producerRecord);

        RecordMetadata recordMetadata = future.get();
        LOGGER.info("Message produced for topic {} at offset {} at partition {}",
                recordMetadata.topic(),
                recordMetadata.offset(),
                recordMetadata.partition()
        );
    }
}
