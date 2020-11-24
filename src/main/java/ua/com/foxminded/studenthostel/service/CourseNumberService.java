package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.repository.CourseNumberRepository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class CourseNumberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseNumberService.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private CourseNumberRepository courseNumberRepository;

    @Autowired
    private ValidatorEntity<CourseNumber> validator;

    public BigInteger insert(CourseNumber courseNumber) {
        LOGGER.debug("inserting {}", courseNumber);

        validator.validate(courseNumber);

        try {
            return courseNumberRepository.save(courseNumber).getId();
        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", courseNumber, ex);
            throw new DaoException("Insertion error : " + courseNumber, ex);
        }
    }

    public CourseNumber getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        return courseNumberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + id));
    }

    public List<CourseNumber> getAll(int pageNumber) {
        LOGGER.debug("getting all, pageNumber = {}, pageSize = {} ", pageNumber, PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<CourseNumber> result = courseNumberRepository.findAll(pageable).getContent();

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, pageNumber = {} ", pageNumber);
            throw new NotFoundException("Result with pageNumber =" + pageNumber + " is empty");
        }
        return result;
    }

    public CourseNumber update(CourseNumber courseNumber) {
        LOGGER.debug("updating {}", courseNumber);

        validator.validate(courseNumber);
        validator.validateId(courseNumber.getId());
        validateExistence(courseNumber.getId());

        try {
            return courseNumberRepository.save(courseNumber);
        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", courseNumber, ex);
            throw new DaoException("Updating errorRR: " + courseNumber, ex);
        }
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);
        try {
            courseNumberRepository.deleteById(id);

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);

        if (!courseNumberRepository.existsById(id)) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist");
        }
    }
}
