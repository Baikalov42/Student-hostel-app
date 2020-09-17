package ua.com.foxminded.studenthostel.service.utils;


import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.math.BigInteger;
import java.util.Set;

@Component
public class ValidatorEntity<T> {

    private javax.validation.Validator getValidatorInstance() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    public void validate(@Valid T t) throws ValidationException {
        Set<ConstraintViolation<T>> validates = getValidatorInstance().validate(t);
        if (!validates.isEmpty()) {
            throw new ValidationException("some problems " + validates.iterator().next());
        }
    }

    public void validateId(BigInteger id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("id cant be null");
        }
        if (id.longValue() == 0) {
            throw new ValidationException("id cant be zero");
        }
    }

    public void validateId(BigInteger firstId, BigInteger secondId) throws ValidationException {
        if (firstId == null || secondId == null) {
            throw new ValidationException("id cant be null");
        }
        if (firstId.longValue() == 0 || secondId.longValue() == 0) {
            throw new ValidationException("id cant be zero");
        }
    }
}

