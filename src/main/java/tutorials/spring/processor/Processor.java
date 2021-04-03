package tutorials.spring.processor;

import tutorials.exception.TutorialException;
import tutorials.spring.publishers.Publisher;
import tutorials.spring.scanner.MessageScanner;

import java.util.List;

public class Processor<T> {

    private final List<MessageScanner<T>> scanners;
    private final Publisher<T> publisher;

    public Processor(List<MessageScanner<T>> scanners, Publisher<T> publisher) {
        this.scanners = scanners;
        this.publisher = publisher;
    }

    public void process(T message) throws TutorialException {

        // run the validation
        scanners
                .forEach(validator -> validator.scan(message));

        // forward the message
        publisher.publish(message);
    }
}
