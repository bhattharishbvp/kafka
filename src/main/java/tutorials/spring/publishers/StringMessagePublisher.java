package tutorials.spring.publishers;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;
import tutorials.exception.TutorialException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class StringMessagePublisher implements Publisher<String> {

    private final static Logger LOGGER = LoggerFactory.getLogger(StringMessagePublisher.class);

    private final String key;
    private KafkaTemplate kafkaTemplate;
    private String topic;

    public StringMessagePublisher(KafkaTemplate kafkaTemplate,
                                  String topic,
                                  String key) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.key = key;
    }

    @Override
    @Transactional //required
    public void publish(String message) throws TutorialException {
        LOGGER.info("Publishing message");

        Future<SendResult> recordMetadataFuture = kafkaTemplate.send(topic, key, "forwarded message | " + message);
        SendResult sendResult = null;
        RecordMetadata recordMetadata = null;
        try {
            sendResult = recordMetadataFuture.get();
            recordMetadata = sendResult.getRecordMetadata();
        } catch (InterruptedException | ExecutionException e) {
            throw new TutorialException(e.getMessage());
        }

        LOGGER.info("Message produced on topic {}, at partition {}, offset {} and key {}",
                recordMetadata.topic(),
                recordMetadata.partition(),
                recordMetadata.offset(),
                sendResult.getProducerRecord().key());

    }
}
