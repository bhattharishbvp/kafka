package tutorials.spring.scanner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tutorials.exception.TutorialException;
import tutorials.exception.TutorialRuntimeException;
import tutorials.spring.publishers.Publisher;

@Component
@Order(2)
public class ConfidentialContentMessageScanner implements MessageScanner<String> {

    @Override
    public void scan(String message) {
        if(StringUtils.isNotBlank(message) && containsConfidentialContent(message)) {
            throw new TutorialRuntimeException("Contains confidential");
        }
    }

    private boolean containsConfidentialContent(String message) {
        return message.contains("confidential");
    }

}
