package tutorials.exception;

public class TutorialKafkaRetryRuntimeException extends RuntimeException {

    public TutorialKafkaRetryRuntimeException(String message) {
        super(message);
    }
}
