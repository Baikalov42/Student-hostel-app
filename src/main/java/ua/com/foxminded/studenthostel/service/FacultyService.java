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


    public List<Faculty> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);
        List<Faculty> result = facultyDao.getAll(limit, offset);

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, limit = {}, offset = {}", limit, offset);
            throw new NotFoundException("Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean update(Faculty faculty) {
        LOGGER.debug("updating {}", faculty);

        validator.validate(faculty);
        validator.validateId(faculty.getId());
        validateExistence(faculty.getId());

        return facultyDao.update(faculty);
    }

    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        return facultyDao.deleteById(id);
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
