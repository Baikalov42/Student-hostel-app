package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.repository.FloorRepository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class FloorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FloorService.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private FloorRepository floorRepository;
    @Autowired
    private ValidatorEntity<Floor> validator;

    public BigInteger insert(Floor floor) {
        LOGGER.debug("inserting {}", floor);

        validator.validate(floor);
        try {
            return floorRepository.save(floor).getId();
        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", floor, ex);
            throw new DaoException("Insertion error : " + floor, ex);
        }
    }

    public Floor getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);

        return floorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + id));
    }

    public List<Floor> getAll(int pageNumber) {
        LOGGER.debug("getting all, pageNumber = {}, pageSize = {} ", pageNumber, PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<Floor> result = floorRepository.findAll(pageable).getContent();

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, pageNumber = {} ", pageNumber);
            throw new NotFoundException("Result with pageNumber =" + pageNumber + " is empty");
        }
        return result;
    }

    public Floor update(Floor floor) {
        LOGGER.debug("updating {}", floor);

        validator.validate(floor);
        validator.validateId(floor.getId());
        validateExistence(floor.getId());

        try {
            return floorRepository.save(floor);
        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", floor, ex);
            throw new DaoException("Updating error: " + floor, ex);
        }
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        try {
            floorRepository.deleteById(id);
        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);

        if (!floorRepository.existsById(id)) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist");
        }
    }
}
