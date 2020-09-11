package ua.com.foxminded.studenthostel.service.utils;

import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.exception.ValidationException;

import java.math.BigInteger;

@Component
public class ValidationsUtils {

    private ValidationsUtils() {
    }

    public static void validateName(String string, String PATTERN) throws ValidationException {
        if (string == null) {
            throw new IllegalArgumentException("name can't be null");
        }
        if (!string.matches(PATTERN)) {
            throw new ValidationException("wrong name format");
        }
    }

    public static void validaHoursDebt(int fromStudent, int max, int min) throws ValidationException {
        if (fromStudent > max || fromStudent < min) {
            throw new ValidationException("incorrect hours value");
        }
    }

    public static void validateId(BigInteger number) throws ValidationException {
        if (number == null) {
            throw new ValidationException("id cant' be null");
        }
        if (number.longValue() == 0) {
            throw new ValidationException("id can't be zero");
        }
    }
}
