package tutorials.spring.scanner;

public interface MessageScanner<T> {

    void scan(T message);
}
