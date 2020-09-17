package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class FloorService {

    @Autowired
    private FloorDao floorDao;

    @Autowired
    private ValidatorEntity<Floor> validator;


    public BigInteger insert(Floor floor) throws ValidationException {

        validator.validate(floor);
        return floorDao.insert(floor);
    }

    public Floor getById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        return floorDao.getById(id);
    }


    public List<Floor> getAll(long limit, long offset) throws ValidationException {
        List<Floor> result = floorDao.getAll(limit, offset);
        if (result.isEmpty()) {
            throw new ValidationException(
                    "Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean update(Floor floor) throws ValidationException {

        validator.validate(floor);
        validator.validateId(floor.getId());
        validateExistence(floor.getId());

        return floorDao.update(floor);
    }


    public boolean deleteById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        validateExistence(id);

        return floorDao.deleteById(id);
    }

    protected void validateExistence(BigInteger id) throws ValidationException {
        try {
            floorDao.getById(id);
        } catch (DaoException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
