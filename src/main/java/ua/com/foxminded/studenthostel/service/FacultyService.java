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

        validator.validateEntity(faculty);
        return facultyDao.insert(faculty);
    }

    public Faculty getById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        return facultyDao.getById(id);
    }


    public List<Faculty> getAll(long limit, long offset) throws ValidationException {

        long countOfEntries = facultyDao.getEntriesCount().longValue();
        if (countOfEntries <= offset) {
            throw new ValidationException("offset is greater than the number of entries");
        }
        return facultyDao.getAll(limit, offset);
    }

    public boolean update(Faculty faculty) throws ValidationException {

        validator.validateEntity(faculty);
        validator.validateId(faculty.getId());
        validateForExist(faculty.getId());

        return facultyDao.update(faculty);
    }


    public boolean deleteById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        validateForExist(id);

        return facultyDao.deleteById(id);
    }

    protected void validateForExist(BigInteger id) throws ValidationException {
        try {
            facultyDao.getById(id);
        } catch (DaoException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
