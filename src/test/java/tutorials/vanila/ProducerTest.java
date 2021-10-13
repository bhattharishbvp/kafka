package tutorials.vanila;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tutorials.vanila.configuration.KafkaConfiguration;

import java.util.concurrent.ExecutionException;

public class ProducerTest {

    public static final String TOPIC = "tutorials-test";
    KafkaProducer<String, String> kafkaProducer;
    KafkaConsumer<String, String> kafkaConsumer;

    @BeforeEach
    public void beforeEach() {
        kafkaProducer = new KafkaProducer<>(KafkaConfiguration.producerConfiguration());
        kafkaConsumer = new KafkaConsumer<>(KafkaConfiguration.consumerConfiguration());
    }

    @Test
    public void demo() throws ExecutionException, InterruptedException {
        String randomMessage = "This is my random message " + Math.random();
        System.out.println("Random message is " + randomMessage);
        StringMessageProducer stringMessageProducer =
                new StringMessageProducer(TOPIC, kafkaProducer);

        stringMessageProducer.produce(randomMessage);

        StringConsumer stringConsumer = new StringConsumer(kafkaConsumer, TOPIC);
        stringConsumer.consume();
    }
}
