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

        validator.validateEntity(floor);
        return floorDao.insert(floor);
    }

    public Floor getById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        return floorDao.getById(id);
    }


    public List<Floor> getAll(long limit, long offset) throws ValidationException {
        long countOfEntries = floorDao.getEntriesCount().longValue();

        if (countOfEntries <= offset) {
            throw new ValidationException("offset is greater than the number of entries");
        }
        return floorDao.getAll(limit, offset);
    }

    public boolean update(Floor floor) throws ValidationException {

        validator.validateEntity(floor);
        validator.validateId(floor.getId());
        validateForExist(floor.getId());

        return floorDao.update(floor);
    }


    public boolean deleteById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        validateForExist(id);

        return floorDao.deleteById(id);
    }

    protected void validateForExist(BigInteger id) throws ValidationException {
        try {
            floorDao.getById(id);
        } catch (DaoException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
