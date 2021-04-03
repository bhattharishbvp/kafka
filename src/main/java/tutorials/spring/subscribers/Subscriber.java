package tutorials.spring.subscribers;

import tutorials.exception.TutorialException;

public interface Subscriber<T> {

    void consume(T message) throws TutorialException;
}
