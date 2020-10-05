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
        LOGGER.debug("inserting {}", room);

        validator.validate(room);
        floorService.validateExistence(room.getFloorId());

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

    public RoomDTO getDTOById(BigInteger id) {
        LOGGER.debug("getting DTO by id {}", id);

        Room room = getById(id);
        RoomDTO roomDTO = getDTO(room);

        LOGGER.debug("getting DTO by id, complete {}", roomDTO);
        return roomDTO;
    }


    public List<Room> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);
        List<Room> result = roomDao.getAll(limit, offset);

        if (result.isEmpty()) {

            LOGGER.warn("result is empty, limit = {}, offset = {}", limit, offset);
            throw new NotFoundException("Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public List<RoomDTO> getAllDTO(long limit, long offset) {
        LOGGER.debug("getting all DTO, limit {} , offset {} ", limit, offset);

        List<Room> rooms = getAll(limit, offset);
        List<RoomDTO> roomDTOS = new ArrayList<>();

        for (Room room : rooms) {
            roomDTOS.add(getDTO(room));
        }
        return roomDTOS;
    }


    public List<RoomDTO> getAllByEquipment(BigInteger equipmentId) {
        LOGGER.debug("getting all by Equipment, id = {} ", equipmentId);

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
        LOGGER.debug("updating {}", room);

        validator.validate(room);
        validator.validateId(room.getId());

        validateExistence(room.getId());
        floorService.validateExistence(room.getFloorId());

        return roomDao.update(room);
    }

    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        return roomDao.deleteById(id);
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

    RoomDTO getDTO(Room room) {
        LOGGER.debug("getting DTO,  {}", room);

        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        roomDTO.setFloor(floorDao.getById(room.getFloorId()));

        LOGGER.debug("getting DTO complete,  {}", roomDTO);
        return roomDTO;
    }
}
