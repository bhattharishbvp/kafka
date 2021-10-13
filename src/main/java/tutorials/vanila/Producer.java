package tutorials.vanila;

import java.util.concurrent.ExecutionException;

public interface Producer<T> {
    void produce(T object) throws ExecutionException, InterruptedException;
}
