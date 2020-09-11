package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.service.utils.ValidationsUtils;

import java.math.BigInteger;
import java.util.List;

@Service
public class FacultyService {

    private static final String NAME_PATTERN = "[A-Z][a-z\\s]{1,29}";

    @Autowired
    FacultyDao facultyDao;


    public BigInteger insert(Faculty faculty) throws ValidationException {

        ValidationsUtils.validateName(faculty.getName(), NAME_PATTERN);
        return facultyDao.insert(faculty);
    }

    public Faculty getById(BigInteger id) throws ValidationException {

        ValidationsUtils.validateId(id);
        return facultyDao.getById(id);
    }


    public List<Faculty> getAll(long limit, long offset) {
        return facultyDao.getAll(limit, offset);
    }

    public boolean update(Faculty faculty) throws ValidationException {

        ValidationsUtils.validateName(faculty.getName(), NAME_PATTERN);
        return facultyDao.update(faculty);
    }


    public boolean deleteById(BigInteger id)  {
        return facultyDao.deleteById(id);
    }
}
