package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
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

    public BigInteger insert(Faculty faculty) throws ValidationException {

        validator.validate(faculty);
        return facultyDao.insert(faculty);
    }

    public Faculty getById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        return facultyDao.getById(id);
    }


    public List<Faculty> getAll(long limit, long offset) throws ValidationException {
        List<Faculty> result = facultyDao.getAll(limit, offset);
        if (result.isEmpty()) {
            throw new ValidationException(
                    "Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean update(Faculty faculty) throws ValidationException {

        validator.validate(faculty);
        validator.validateId(faculty.getId());
        validateExistence(faculty.getId());

        return facultyDao.update(faculty);
    }


    public boolean deleteById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        validateExistence(id);

        return facultyDao.deleteById(id);
    }

    protected void validateExistence(BigInteger id) throws ValidationException {
        try {
            facultyDao.getById(id);
        } catch (DaoException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
