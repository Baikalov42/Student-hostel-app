package ua.com.foxminded.studenthostel.dao;


import ua.com.foxminded.studenthostel.models.Floor;

import java.math.BigInteger;
import java.util.List;

public interface FloorDao {
    BigInteger insert(Floor floor);

    Floor getById(BigInteger floorId);

    List<Floor> getAll(long limit, long offset);

    boolean update(Floor floor);

    boolean deleteById(BigInteger id);
}
