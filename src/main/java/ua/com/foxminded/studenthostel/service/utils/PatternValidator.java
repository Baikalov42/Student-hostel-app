package ua.com.foxminded.studenthostel.service.utils;

import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.exception.ValidationException;

@Component
public class PatternValidator {

    public static void validateName(String string, String PATTERN) throws ValidationException {
        if (string == null) {
            throw new IllegalArgumentException("id can't be null");
        } else if (!string.matches(PATTERN)) {
            throw new ValidationException("wrong name format");
        }
    }

    public static void validaHoursDebt(int fromStudent, int max, int min) throws ValidationException {
        if (fromStudent > max || fromStudent < min) {
            throw new ValidationException("incorrect hours value");
        }
    }
}
