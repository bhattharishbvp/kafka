package tutorials.spring.subscribers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tutorials.spring.processor.Processor;

@Configuration
public class SubscriberConfiguration {

    @Bean
    public Subscriber stringMessageSubscriber(Processor stringMessageProcessor) {
        return new StringMessageSubscriber(stringMessageProcessor);
    }
}
