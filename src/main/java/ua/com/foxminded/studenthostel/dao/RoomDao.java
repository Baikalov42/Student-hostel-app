package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.mappers.RoomMapper;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoomDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Qualifier("roomJdbcInsert")
    @Autowired
    private SimpleJdbcInsert roomJdbcInsert;

    public BigInteger insert(Room room) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("room_name", room.getName());
        parameters.put("floor_id", room.getFloorId());

        return BigInteger.valueOf(roomJdbcInsert.executeAndReturnKey(parameters).longValue());
    }

    public Room getById(BigInteger roomId) {
        String query = "" +
                "SELECT * " +
                "FROM rooms " +
                "WHERE room_id = ? ";
        return jdbcTemplate.queryForObject(query, new RoomMapper(), roomId);
    }

    public List<Room> getAllByEquipment(BigInteger equipmentId) {
        String query = "" +
                "SELECT * FROM equipments " +
                "INNER JOIN students_equipments ON equipments.equipments_id = students_equipments.equipment_id " +
                "INNER JOIN students ON students_equipments.student_id = students.student_id " +
                "INNER JOIN rooms ON students.room_id = rooms.room_id " +
                "WHERE equipments_id = ? ";
        return jdbcTemplate.query(query, new RoomMapper(), equipmentId);
    }

    public boolean changeRoom(BigInteger newRoomId, BigInteger studentId) {
        String query = "" +
                "UPDATE students " +
                "SET room_id = ? " +
                "WHERE student_id = ? ";
        return jdbcTemplate.update(query, newRoomId, studentId) == 1;
    }

    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM rooms " +
                "WHERE room_id  = ? ";

        return jdbcTemplate.update(query, id) == 1;
    }
}
