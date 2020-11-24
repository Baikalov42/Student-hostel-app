package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.repository.FacultyRepository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class FacultyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyService.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private ValidatorEntity<Faculty> validator;

    public BigInteger insert(Faculty faculty) {
        LOGGER.debug("inserting {}", faculty);

        validator.validate(faculty);
        try {
            return facultyRepository.save(faculty).getId();
        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", faculty, ex);
            throw new DaoException("Insertion error : " + faculty, ex);
        }
    }

    public Faculty getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);

        return facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + id));
    }

    public List<Faculty> getAll(int pageNumber) {
        LOGGER.debug("getting all, pageNumber = {}, pageSize = {} ", pageNumber, PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<Faculty> result = facultyRepository.findAll(pageable).getContent();

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, pageNumber = {} ", pageNumber);
            throw new NotFoundException("Result with pageNumber =" + pageNumber + " is empty");
        }
        return result;
    }

    public Faculty update(Faculty faculty) {
        LOGGER.debug("updating {}", faculty);

        validator.validate(faculty);
        validator.validateId(faculty.getId());
        validateExistence(faculty.getId());

        try {
            return facultyRepository.save(faculty);
        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", faculty, ex);
            throw new DaoException("Updating error: " + faculty, ex);
        }
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);
        try {
            facultyRepository.deleteById(id);
        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);

        if (!facultyRepository.existsById(id)) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist");
        }
    }
}
