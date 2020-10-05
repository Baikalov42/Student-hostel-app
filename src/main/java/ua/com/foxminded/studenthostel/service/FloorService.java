package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(FloorService.class);

    @Autowired
    private FloorDao floorDao;

    @Autowired
    private ValidatorEntity<Floor> validator;


    public BigInteger insert(Floor floor) {
        LOGGER.debug("inserting {}", floor);

        validator.validate(floor);
        BigInteger id = floorDao.insert(floor);

        LOGGER.debug("inserting complete, id = {}", id);
        return id;
    }

    public Floor getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        Floor floor = floorDao.getById(id);

        LOGGER.debug("getting complete {}", floor);
        return floor;
    }


    public List<Floor> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);
        List<Floor> result = floorDao.getAll(limit, offset);

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, limit = {}, offset = {}", limit, offset);
            throw new NotFoundException("Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean update(Floor floor) {
        LOGGER.debug("updating {}", floor);

        validator.validate(floor);
        validator.validateId(floor.getId());
        validateExistence(floor.getId());

        return floorDao.update(floor);
    }

    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        return floorDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);
        try {
            floorDao.getById(id);

        } catch (NotFoundException ex) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
