package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.Room;

import java.math.BigInteger;
import java.util.List;

public interface RoomDao {

    BigInteger insert(Room room);

    Room getById(BigInteger roomId);

    List<Room> getAll(int offset, int limit);

    List<Room> getAllByEquipment(BigInteger equipmentId);

    Room update(Room room);

    void deleteById(BigInteger id);
}
