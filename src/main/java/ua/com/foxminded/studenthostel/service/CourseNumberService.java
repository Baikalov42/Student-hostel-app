package ua.com.foxminded.studenthostel.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;


import java.math.BigInteger;
import java.util.List;


@Service
public class CourseNumberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseNumberService.class);

    @Autowired
    private CourseNumberDao courseNumberDao;

    @Autowired
    private ValidatorEntity<CourseNumber> validator;

    public BigInteger insert(CourseNumber courseNumber) {
        LOGGER.debug("inserting {}", courseNumber);

        validator.validate(courseNumber);
        BigInteger id = courseNumberDao.insert(courseNumber);

        LOGGER.debug("inserting complete, id = {}", id);
        return id;
    }

    public CourseNumber getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        CourseNumber courseNumber = courseNumberDao.getById(id);

        LOGGER.debug("getting complete {}", courseNumber);
        return courseNumber;
    }

    public List<CourseNumber> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);

        List<CourseNumber> result = courseNumberDao.getAll(limit, offset);

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, limit = {}, offset = {}", limit, offset);
            throw new NotFoundException("Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean update(CourseNumber courseNumber) {

        LOGGER.debug("updating {}", courseNumber);

        validator.validate(courseNumber);
        validator.validateId(courseNumber.getId());
        validateExistence(courseNumber.getId());

        return courseNumberDao.update(courseNumber);
    }

    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        return courseNumberDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);
        try {
            courseNumberDao.getById(id);

        } catch (NotFoundException ex) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
