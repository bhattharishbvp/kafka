package tutorials.spring.publishers;

import tutorials.exception.TutorialException;

import java.util.concurrent.ExecutionException;

public interface Publisher<T> {

    void publish(T message) throws TutorialException;
}
