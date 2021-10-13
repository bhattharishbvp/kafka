package tutorials.spring.configuration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tutorials.exception.TutorialException;
import tutorials.spring.publishers.Publisher;

@Component
public class MessageRecover {
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageRecover.class);

    private final Publisher<String> dlqStringMessagePublisher;

    public MessageRecover(Publisher dlqStringMessagePublisher) {
        this.dlqStringMessagePublisher = dlqStringMessagePublisher;
    }

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
