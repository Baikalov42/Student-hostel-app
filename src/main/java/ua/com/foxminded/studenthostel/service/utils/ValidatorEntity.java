package ua.com.foxminded.studenthostel.service.utils;


import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import java.math.BigInteger;
import java.util.Set;

@Component
public class ValidatorEntity<T> {

    private javax.validation.Validator getValidatorInstance() {

        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

    public void validate(@Valid T t) {
        Set<ConstraintViolation<T>> validates;

        try {
            validates = getValidatorInstance().validate(t);

        } catch (IllegalArgumentException ex) {
            throw new ValidationException("some problems", ex);
        }
        if (!validates.isEmpty()) {
            throw new ValidationException("some problems " + validates.iterator().next());
        }
    }

    public void validateId(BigInteger id) {
        if (id == null) {
            throw new ValidationException("id cant be null");
        }
        if (id.longValue() < 1) {
            throw new ValidationException("id must be greater than 0");
        }

    }

    public void validateId(BigInteger firstId, BigInteger secondId) {
        if (firstId == null || secondId == null) {
            throw new ValidationException("id cant be null");
        }
        if (firstId.longValue() < 1 || secondId.longValue() < 1) {
            throw new ValidationException("id cant be zero");
        }
    }
}

