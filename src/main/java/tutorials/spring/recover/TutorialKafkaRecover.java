package tutorials.spring.recover;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface TutorialKafkaRecover {
    void recover(ConsumerRecord message);
}
