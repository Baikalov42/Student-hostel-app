package ua.com.foxminded.studenthostel.service.utils;


import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import java.math.BigInteger;
import java.util.Set;

@Component
public class ValidatorEntity<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorEntity.class);

    private javax.validation.Validator getValidatorInstance() {

        return Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

    public void validate(@Valid T t) {
        LOGGER.debug("trying to check whether object is valid: {}", t);

        Set<ConstraintViolation<T>> validates;

        try {
            validates = getValidatorInstance().validate(t);

        } catch (IllegalArgumentException ex) {
            LOGGER.warn("object not valid: {}", t, ex);
            throw new ValidationException("object not valid", ex);
        }
        if (!validates.isEmpty()) {
            LOGGER.warn("not valid: {}", validates.iterator().next());
            throw new ValidationException("not valid " + validates.iterator().next());
        }
    }

    public void validateId(BigInteger id) {
        LOGGER.debug("id validation , id = {}", id);

        if (id == null) {
            LOGGER.warn("not valid, id is null");
            throw new ValidationException("id cant be null");
        }
        if (id.longValue() < 1) {
            LOGGER.warn("not valid, id < 1, id = {}", id);
            throw new ValidationException("id must be greater than 0");
        }

    }

    public void validateId(BigInteger firstId, BigInteger secondId) {
        LOGGER.debug("id validation , first id = {} , second id = {}", firstId, secondId);

        if (firstId == null || secondId == null) {
            LOGGER.warn("not valid, id is null");
            throw new ValidationException("id cant be null");
        }
        if (firstId.longValue() < 1 || secondId.longValue() < 1) {
            LOGGER.warn("not valid, id < 1. First id = {} , second id = {}", firstId, secondId);
            throw new ValidationException("id cant be zero");
        }
    }
}

