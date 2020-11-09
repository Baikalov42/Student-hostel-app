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


    public List<Floor> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {} , limit {} ", offset, limit);
        List<Floor> result = floorDao.getAll(offset, limit);

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, offset = {}, limit = {}", offset, limit);
            throw new NotFoundException("Result with offset=" + offset + " and limit=" + limit + " is empty");
        }
        return result;
    }

    public Floor update(Floor floor) {
        LOGGER.debug("updating {}", floor);

        validator.validate(floor);
        validator.validateId(floor.getId());
        validateExistence(floor.getId());

        return floorDao.update(floor);
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

         floorDao.deleteById(id);
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
