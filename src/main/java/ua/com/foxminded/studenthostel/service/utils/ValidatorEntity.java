package ua.com.foxminded.studenthostel.service.utils;


import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.exception.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
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
}

