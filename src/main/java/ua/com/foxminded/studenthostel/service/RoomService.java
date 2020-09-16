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

        validator.validateEntity(room);
        return roomDao.insert(room);
    }

    public RoomDTO getById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        Room room = roomDao.getById(id);

        return getDTO(room);
    }


    public List<RoomDTO> getAll(long limit, long offset) throws ValidationException {
        long countOfEntries = roomDao.getEntriesCount().longValue();

        if (countOfEntries <= offset) {
            throw new ValidationException("offset is greater than the number of entries");
        }
        List<Room> rooms = roomDao.getAll(limit, offset);
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
        validator.validateEntity(room);
        validator.validateId(room.getId());
        validateForExist(room.getId());

        return roomDao.update(room);
    }

    public boolean deleteById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        validateForExist(id);

        return roomDao.deleteById(id);
    }

    protected void validateForExist(BigInteger id) throws ValidationException {
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
