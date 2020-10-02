package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.dao.RoomDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.dto.RoomDTO;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomService.class);

    @Autowired
    private RoomDao roomDao;
    @Autowired
    private FloorDao floorDao;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private FloorService floorService;
    @Autowired
    private ValidatorEntity<Room> validator;

    public BigInteger insert(Room room) {

        validator.validate(room);
        floorService.validateExistence(room.getFloorId());

        return roomDao.insert(room);
    }

    public Room getById(BigInteger id) {

        validator.validateId(id);
        return roomDao.getById(id);
    }

    public RoomDTO getDTOById(BigInteger id) {

        Room room = getById(id);
        return getDTO(room);
    }


    public List<Room> getAll(long limit, long offset) {

        List<Room> result = roomDao.getAll(limit, offset);

        if (result.isEmpty()) {

            LOGGER.warn("result is empty, limit = {}, offset = {}", limit, offset);
            throw new NotFoundException("Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public List<RoomDTO> getAllDTO(long limit, long offset) {

        List<Room> rooms = getAll(limit, offset);
        List<RoomDTO> roomDTOS = new ArrayList<>();

        for (Room room : rooms) {
            roomDTOS.add(getDTO(room));
        }
        return roomDTOS;
    }


    public List<RoomDTO> getAllByEquipment(BigInteger equipmentId) {
        validator.validateId(equipmentId);
        equipmentService.validateExistence(equipmentId);

        List<Room> rooms = roomDao.getAllByEquipment(equipmentId);

        if (rooms.isEmpty()) {

            LOGGER.warn("Result is empty, equipment id = {}", equipmentId);
            throw new NotFoundException("Result with equipment id=" + equipmentId + " is empty");
        }
        List<RoomDTO> roomDTOS = new ArrayList<>();

        for (Room room : rooms) {
            roomDTOS.add(getDTO(room));
        }
        return roomDTOS;
    }

    public boolean update(Room room) {
        validator.validate(room);
        validator.validateId(room.getId());

        validateExistence(room.getId());
        floorService.validateExistence(room.getFloorId());

        return roomDao.update(room);
    }

    public boolean deleteById(BigInteger id) {

        validator.validateId(id);
        validateExistence(id);

        return roomDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);

        try {
            roomDao.getById(id);
        } catch (NotFoundException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }

    RoomDTO getDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        roomDTO.setFloor(floorDao.getById(room.getFloorId()));

        return roomDTO;
    }
}
