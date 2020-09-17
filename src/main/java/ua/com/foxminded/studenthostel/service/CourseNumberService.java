package ua.com.foxminded.studenthostel.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;


import java.math.BigInteger;
import java.util.List;


@Service
public class CourseNumberService {

    @Autowired
    private CourseNumberDao courseNumberDao;

    @Autowired
    private ValidatorEntity<CourseNumber> validator;

    public BigInteger insert(CourseNumber courseNumber) throws ValidationException {

        validator.validate(courseNumber);
        return courseNumberDao.insert(courseNumber);
    }

    public CourseNumber getById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        return courseNumberDao.getById(id);
    }

    public List<CourseNumber> getAll(long limit, long offset) throws ValidationException {
        List<CourseNumber> result = courseNumberDao.getAll(limit, offset);
        if(result.isEmpty()){
            throw new ValidationException(
                    "Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean update(CourseNumber courseNumber) throws ValidationException {

        validator.validate(courseNumber);
        validator.validateId(courseNumber.getId());
        validateExistence(courseNumber.getId());

        return courseNumberDao.update(courseNumber);
    }

    public boolean deleteById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        validateExistence(id);

        return courseNumberDao.deleteById(id);
    }

    protected void validateExistence(BigInteger id) throws ValidationException {
        try {
            courseNumberDao.getById(id);
        } catch (DaoException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }

}
