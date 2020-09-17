package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.dao.RoomDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.dto.RoomDTO;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomDao roomDao;
    @Autowired
    private FloorDao floorDao;
    @Autowired
    private ValidatorEntity<Room> validator;


    public BigInteger insert(Room room) throws ValidationException {

        validator.validate(room);
        return roomDao.insert(room);
    }

    public Room getById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        return roomDao.getById(id);
    }

    public RoomDTO getDTOById(BigInteger id) throws ValidationException {

        Room room = getById(id);
        return getDTO(room);
    }


    public List<Room> getAll(long limit, long offset) throws ValidationException {

        List<Room> result = roomDao.getAll(limit, offset);
        if (result.isEmpty()) {
            throw new ValidationException(
                    "Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public List<RoomDTO> getAllDTO(long limit, long offset) throws ValidationException {

        List<Room> rooms = getAll(limit, offset);
        List<RoomDTO> roomDTOS = new ArrayList<>();

        for (Room room : rooms) {
            roomDTOS.add(getDTO(room));
        }
        return roomDTOS;
    }


    public List<RoomDTO> getAllByEquipment(BigInteger equipmentId) throws ValidationException {
        validator.validateId(equipmentId);

        List<Room> rooms = roomDao.getAllByEquipment(equipmentId);
        List<RoomDTO> roomDTOS = new ArrayList<>();

        for (Room room : rooms) {
            roomDTOS.add(getDTO(room));
        }
        return roomDTOS;
    }

    public boolean update(Room room) throws ValidationException {
        validator.validate(room);
        validator.validateId(room.getId());
        validateExistence(room.getId());

        return roomDao.update(room);
    }

    public boolean deleteById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        validateExistence(id);

        return roomDao.deleteById(id);
    }

    void validateExistence(BigInteger id) throws ValidationException {
        try {
            roomDao.getById(id);
        } catch (DaoException ex) {
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
