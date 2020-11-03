package ua.com.foxminded.studenthostel.dao;


import ua.com.foxminded.studenthostel.models.Floor;

import java.math.BigInteger;
import java.util.List;

public interface FloorDao {
    BigInteger insert(Floor floor);

    Floor getById(BigInteger floorId);

    List<Floor> getAll(int offset, int limit);

    Floor update(Floor floor);

    void deleteById(BigInteger id);
}
