package tutorials.spring.scanner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tutorials.exception.TutorialException;
import tutorials.exception.TutorialKafkaRetryRuntimeException;
import tutorials.exception.TutorialRuntimeException;
import tutorials.spring.publishers.Publisher;

@Component
@Order(1)
public class AbusiveContentMessageScanner implements MessageScanner<String> {

    Publisher abusiveMessagePublisher;

    public AbusiveContentMessageScanner(Publisher abusiveMessagePublisher) {
        this.abusiveMessagePublisher = abusiveMessagePublisher;
    }

    @Override
    public void scan(String message) {

        if(StringUtils.isBlank(message))
            return;

        if(containsAbusiveWord(message)) {
            try {
                abusiveMessagePublisher.publish(message);
            } catch (TutorialException e) {
                throw new TutorialRuntimeException(e.getMessage());
            }
        }

        if(message.contains("retry")) {
            throw new TutorialKafkaRetryRuntimeException("Please retry"); // mimic technical error for retry
        }
    }

    private boolean containsAbusiveWord(String message) {
        return message.contains("bad");
    }


}
