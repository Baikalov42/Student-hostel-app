package ua.com.foxminded.studenthostel.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.service.utils.ValidationsUtils;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;


import java.math.BigInteger;
import java.util.List;


@Service
public class CourseNumberService {

    private static final String NAME_PATTERN = "[A-Z][a-z]{3,29}";

    @Autowired
    private CourseNumberDao courseNumberDao;

    @Autowired
    private ValidatorEntity<CourseNumber> validatorEntity;

    public BigInteger insert(CourseNumber courseNumber) throws ValidationException {

        validatorEntity.validate(courseNumber);
        return courseNumberDao.insert(courseNumber);
    }

    public CourseNumber getById(BigInteger id) throws ValidationException {
        ValidationsUtils.validateId(id);
        return courseNumberDao.getById(id);
    }

    public List<CourseNumber> getAll(long limit, long offset) {
        return courseNumberDao.getAll(limit, offset);
    }

    public boolean update(CourseNumber courseNumber) throws ValidationException {

        ValidationsUtils.validateName(courseNumber.getName(), NAME_PATTERN);
        return courseNumberDao.update(courseNumber);
    }

    public boolean deleteById(BigInteger id) {
        return courseNumberDao.deleteById(id);
    }

}
