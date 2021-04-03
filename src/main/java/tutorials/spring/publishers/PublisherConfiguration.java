package tutorials.spring.publishers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class PublisherConfiguration {

    @Bean
    public Publisher<String> stringMessageForwardPublisher(
            KafkaTemplate kafkaTemplate
    ) {
        return new StringMessagePublisher(
                kafkaTemplate,
                "processed-message",
                "forwarded-message"
        );
    }

    @Bean
    public Publisher<String> abusiveMessagePublisher(
            KafkaTemplate kafkaTemplate
    ) {
        return new StringMessagePublisher(
                kafkaTemplate,
                "abusive-message",
                "abusive-message"
        );
    }

    @Bean
    public Publisher<String> dlqStringMessagePublisher(
            KafkaTemplate kafkaTemplate
    ) {
        return new StringMessagePublisher(
                kafkaTemplate,
                "dlq-message",
                "dlq"
        );
    }
}
