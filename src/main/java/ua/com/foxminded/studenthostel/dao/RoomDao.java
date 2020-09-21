package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.Room;

import java.math.BigInteger;
import java.util.List;

public interface RoomDao {
    BigInteger insert(Room room);

    Room getById(BigInteger roomId);

    List<Room> getAll(long limit, long offset);

    List<Room> getAllByEquipment(BigInteger equipmentId);

    BigInteger getEntriesCount();

    boolean update(Room room);

    boolean deleteById(BigInteger id);
}
