package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.repository.RoomRepository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private RoomRepository roomRepository;
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
        try {
            return roomRepository.save(room).getId();

        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", room, ex);
            throw new DaoException("Insertion error : " + room, ex);
        }
    }

    public Room getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + id));
    }


    public List<Room> getAll(int pageNumber) {
        LOGGER.debug("getting all, pageNumber = {}, pageSize = {} ", pageNumber, PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<Room> result = roomRepository.findAll(pageable).getContent();

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, pageNumber = {} ", pageNumber);
            throw new NotFoundException("Result with pageNumber =" + pageNumber + " is empty");
        }
        return result;
    }


    public List<Room> getAllByEquipment(BigInteger equipmentId) {
        LOGGER.debug("getting all by Equipment, id = {} ", equipmentId);

        validator.validateId(equipmentId);
        equipmentService.validateExistence(equipmentId);

        List<Room> rooms = roomRepository.getAllByEquipment(equipmentId);

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
        floorService.validateExistence(room
                .getFloor()
                .getId());
        try {
            return roomRepository.save(room);

        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", room, ex);
            throw new DaoException("Updating error: " + room, ex);
        }
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);
        try {
            roomRepository.deleteById(id);

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);

        if (!roomRepository.existsById(id)) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist");
        }
    }
}
