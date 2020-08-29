package ua.com.foxminded.studenthostel.models.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Room;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RoomMapper implements RowMapper<Room> {

    @Override
    public Room mapRow(ResultSet resultSet, int i) throws SQLException {
        Room room = new Room();

        room.setName(resultSet.getString("room_name"));
        room.setFloorId(BigInteger.valueOf(resultSet.getInt("floor_id")));
        room.setId(BigInteger.valueOf(resultSet.getInt("room_id")));

        return room;
    }
}
