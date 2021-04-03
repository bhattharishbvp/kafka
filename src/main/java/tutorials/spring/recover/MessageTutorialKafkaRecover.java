package tutorials.spring.recover;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tutorials.exception.TutorialException;
import tutorials.spring.publishers.Publisher;

@Component
public class MessageTutorialKafkaRecover implements TutorialKafkaRecover {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageTutorialKafkaRecover.class);

    private final Publisher<String> dlqStringMessagePublisher;

    public MessageTutorialKafkaRecover(Publisher dlqStringMessagePublisher) {
        this.dlqStringMessagePublisher = dlqStringMessagePublisher;
    }

    @Override
    public void recover(ConsumerRecord consumerRecord) {
        Object message = consumerRecord.value();
        LOGGER.info("All retries exhausted for message {} , putting message in DLQ", message);

        try {
            dlqStringMessagePublisher.publish(message.toString());
        } catch (TutorialException e) {
            LOGGER.error("Error while publishing message {} into DLQ", message);
        }
    }
}
