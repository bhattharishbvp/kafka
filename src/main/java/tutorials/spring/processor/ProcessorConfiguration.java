package tutorials.spring.processor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tutorials.spring.publishers.Publisher;
import tutorials.spring.scanner.MessageScanner;

import java.util.List;

@Configuration
public class ProcessorConfiguration {

    @Bean
    public Processor stringMessageProcessor(
            List<MessageScanner> messageScanners,
            Publisher stringMessageForwardPublisher
    ) {
        return new Processor(messageScanners, stringMessageForwardPublisher);
    }
}
