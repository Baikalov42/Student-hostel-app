package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.dao.RoomDao;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.dto.RoomDTO;
import ua.com.foxminded.studenthostel.service.utils.PatternValidator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private static final String NAME_PATTERN = "[A-Z]{2}[-][0-9]{4}";

    @Autowired
    RoomDao roomDao;

    @Autowired
    FloorDao floorDao;

    public BigInteger insert(Room room) throws ValidationException {
        PatternValidator.validateName(room.getName(), NAME_PATTERN);
        return roomDao.insert(room);
    }

    public RoomDTO getById(BigInteger id) {
        if (id == null || id.longValue() == 0) {
            throw new IllegalArgumentException();
        }
        Room room = roomDao.getById(id);
        return getDTO(room);
    }


    public List<RoomDTO> getAll(long limit, long offset) {
        List<Room> rooms = roomDao.getAll(limit, offset);
        List<RoomDTO> roomDTOS = new ArrayList<>();

        for (Room room : rooms) {
            roomDTOS.add(getDTO(room));
        }
        return roomDTOS;
    }

    public List<RoomDTO> getAllByEquipment(BigInteger equipmentId) {
        List<Room> rooms = roomDao.getAllByEquipment(equipmentId);
        List<RoomDTO> roomDTOS = new ArrayList<>();

        for (Room room : rooms) {
            roomDTOS.add(getDTO(room));
        }
        return roomDTOS;
    }

    public boolean update(Room room) throws ValidationException {
        PatternValidator.validateName(room.getName(), NAME_PATTERN);
        return roomDao.update(room);
    }

    public boolean deleteById(BigInteger id) {
        return roomDao.deleteById(id);
    }

    RoomDTO getDTO(Room room) {

        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        roomDTO.setFloor(floorDao.getById(room.getFloorId()));

        return roomDTO;
    }
}
