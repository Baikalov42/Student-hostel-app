package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.dao.RoomDao;
import ua.com.foxminded.studenthostel.dao.StudentDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);

    @Autowired
    private RoomDao roomDao;
    @Autowired
    private FloorDao floorDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private FloorService floorService;
    @Autowired
    private ValidatorEntity<Room> validator;

    public BigInteger insert(Room room) {
        LOGGER.debug("inserting {}", room);

        validator.validate(room);
        floorService.validateExistence(room.getFloor().getId());

        BigInteger id = roomDao.insert(room);

        LOGGER.debug("inserting complete, id = {}", id);
        return id;
    }

    public Room getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        Room room = roomDao.getById(id);

        LOGGER.debug("getting complete {}", room);
        return room;
    }


    public List<Room> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {} , limit {} ", offset, limit);
        List<Room> result = roomDao.getAll(offset, limit);

        if (result.isEmpty()) {

            LOGGER.warn("result is empty, offset = {}, limit = {}", offset, limit);
            throw new NotFoundException("Result with offset=" + offset + " and limit=" + limit + " is empty");
        }
        return result;
    }


    public List<Room> getAllByEquipment(BigInteger equipmentId) {
        LOGGER.debug("getting all by Equipment, id = {} ", equipmentId);

        validator.validateId(equipmentId);
        equipmentService.validateExistence(equipmentId);

        List<Room> rooms = roomDao.getAllByEquipment(equipmentId);

        if (rooms.isEmpty()) {

            LOGGER.warn("Result is empty, equipment id = {}", equipmentId);
            throw new NotFoundException("Result with equipment id=" + equipmentId + " is empty");
        }
        return rooms;
    }

    public Room update(Room room) {
        LOGGER.debug("updating {}", room);

        validator.validate(room);
        validator.validateId(room.getId());

        validateExistence(room.getId());
        floorService.validateExistence(room.getFloor().getId());

        return roomDao.update(room);
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        roomDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);
        try {
            roomDao.getById(id);

        } catch (NotFoundException ex) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
