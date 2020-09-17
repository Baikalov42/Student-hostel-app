package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
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


    public BigInteger insert(Floor floor) {

        validator.validate(floor);
        return floorDao.insert(floor);
    }

    public Floor getById(BigInteger id) {

        validator.validateId(id);
        return floorDao.getById(id);
    }


    public List<Floor> getAll(long limit, long offset) {
        List<Floor> result = floorDao.getAll(limit, offset);
        if (result.isEmpty()) {
            throw new NotFoundException(
                    "Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean update(Floor floor) {

        validator.validate(floor);
        validator.validateId(floor.getId());
        validateExistence(floor.getId());

        return floorDao.update(floor);
    }

    public boolean deleteById(BigInteger id) {

        validator.validateId(id);
        validateExistence(id);

        return floorDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        try {
            floorDao.getById(id);
        } catch (NotFoundException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
