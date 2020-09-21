package ua.com.foxminded.studenthostel.service;

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

    @Autowired
    private FacultyDao facultyDao;

    @Autowired
    private ValidatorEntity<Faculty> validator;

    public BigInteger insert(Faculty faculty) {

        validator.validate(faculty);
        return facultyDao.insert(faculty);
    }

    public Faculty getById(BigInteger id) {

        validator.validateId(id);
        return facultyDao.getById(id);
    }


    public List<Faculty> getAll(long limit, long offset) {
        List<Faculty> result = facultyDao.getAll(limit, offset);
        if (result.isEmpty()) {
            throw new NotFoundException(
                    "Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean update(Faculty faculty) {

        validator.validate(faculty);
        validator.validateId(faculty.getId());
        validateExistence(faculty.getId());

        return facultyDao.update(faculty);
    }

    public boolean deleteById(BigInteger id) {

        validator.validateId(id);
        validateExistence(id);

        return facultyDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        try {
            facultyDao.getById(id);
        } catch (NotFoundException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
