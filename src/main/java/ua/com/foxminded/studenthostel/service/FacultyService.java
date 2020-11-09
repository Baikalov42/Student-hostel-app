package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class FacultyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyService.class);

    @Autowired
    private FacultyDao facultyDao;

    @Autowired
    private ValidatorEntity<Faculty> validator;

    public BigInteger insert(Faculty faculty) {
        LOGGER.debug("inserting {}", faculty);

        validator.validate(faculty);
        BigInteger id = facultyDao.insert(faculty);

        LOGGER.debug("inserting complete, id = {}", id);
        return id;
    }

    public Faculty getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        Faculty faculty = facultyDao.getById(id);

        LOGGER.debug("getting complete {}", faculty);
        return faculty;
    }


    public List<Faculty> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {} , limit {} ", offset, limit);
        List<Faculty> result = facultyDao.getAll(offset, limit);

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, offset = {}, limit = {}", offset, limit);
            throw new NotFoundException("Result with offset=" + offset + " and limit=" + limit + " is empty");
        }
        return result;
    }

    public Faculty update(Faculty faculty) {
        LOGGER.debug("updating {}", faculty);

        validator.validate(faculty);
        validator.validateId(faculty.getId());
        validateExistence(faculty.getId());

        return facultyDao.update(faculty);
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        facultyDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);
        try {
            facultyDao.getById(id);

        } catch (NotFoundException ex) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
