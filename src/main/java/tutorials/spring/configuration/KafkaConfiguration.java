package tutorials.spring.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.StickyAssignor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DefaultAfterRollbackProcessor;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer2;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Properties;
import java.util.function.BiConsumer;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Bean
    public ProducerFactory<String, Object> producerFactory(
            @Value("${tutorials.spring.kafka.common.bootstrap.servers}") String bootstrapServers,
            @Value("${tutorials.spring.kafka.publisher.acks}") String acks,
            @Value("${tutorials.spring.kafka.publisher.batchSize}") String batchSize,
            @Value("${tutorials.spring.kafka.publisher.clientId}") String clientId,
            @Value("${tutorials.spring.kafka.publisher.enableIdempotence}") String enableIdempotence,
            @Value("${tutorials.spring.kafka.publisher.transactionIdPrefix}") String transactionIdPrefix
    ) {

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, acks);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence);

        DefaultKafkaProducerFactory<String, Object> defaultKafkaProducerFactory = new DefaultKafkaProducerFactory(properties);
        defaultKafkaProducerFactory.setTransactionIdPrefix(transactionIdPrefix);
        return defaultKafkaProducerFactory;
    }

    @Bean
    public KafkaTemplate kafkaTemplate(ProducerFactory producerFactory) {
        return new KafkaTemplate(producerFactory);
    }

    @Bean
    public PlatformTransactionManager kafkaTransactionManager(ProducerFactory producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory(
            @Value("${tutorials.spring.kafka.common.bootstrap.servers}") String bootstrapServers,
            @Value("${tutorials.spring.kafka.subscriber.enableAutoCommit}") String enableAutoCommit
    ) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, StickyAssignor.class.getName());


        // deserializers
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ErrorHandlingDeserializer2.KEY_DESERIALIZER_CLASS, StringDeserializer.class); // to catch exception in serialized key
        properties.put(ErrorHandlingDeserializer2.VALUE_DESERIALIZER_CLASS, StringDeserializer.class); // to catch exception in serialized value

        return new DefaultKafkaConsumerFactory(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> concurrentKafkaListenerContainerFactory(
            ConsumerFactory consumerFactory,
            PlatformTransactionManager kafkaTransactionManager,
            MessageRecover stringMessageRecover
    ) {

        ConcurrentKafkaListenerContainerFactory<String, Object> concurrentKafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory);

        // retry
        concurrentKafkaListenerContainerFactory.setAfterRollbackProcessor(
                new DefaultAfterRollbackProcessor(recover(stringMessageRecover), new FixedBackOff(1000, 2)));

        //link transaction manager to subscriber
        concurrentKafkaListenerContainerFactory.getContainerProperties().setTransactionManager(kafkaTransactionManager);

        return concurrentKafkaListenerContainerFactory;
    }

    private BiConsumer<ConsumerRecord, Exception> recover(MessageRecover stringMessageRecover) {
        return ((consumerRecord, e) -> {
            stringMessageRecover.recover(consumerRecord);
        });
    }
}
