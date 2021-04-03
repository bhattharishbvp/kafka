package tutorials.spring.subscribers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import tutorials.exception.TutorialException;
import tutorials.spring.processor.Processor;

public class StringMessageSubscriber implements Subscriber<String> {
    private final static Logger LOGGER = LoggerFactory.getLogger(StringMessageSubscriber.class);
    private final Processor processor;

    public StringMessageSubscriber(Processor abusiveMessagePublisher) {
        this.processor = abusiveMessagePublisher;

    }

    @Override
    @KafkaListener(
            topics = "message",
            groupId = "messageConsumer",
            clientIdPrefix = "message-tutorials",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void consume(String message) throws TutorialException {
        LOGGER.info("Received message {}", message);

        processor.process(message);
    }
}
