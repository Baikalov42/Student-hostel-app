package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.mappers.RoomMapper;

import java.math.BigInteger;
import java.util.List;

@Component
public class RoomDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(Room room) {

        String query = "" +
                "INSERT INTO rooms (room_name, floor_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(query, room.getName(), room.getFloorId());
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

    public void changeRoom(BigInteger newRoomId, BigInteger studentId) {
        String query = "" +
                "UPDATE students " +
                "SET room_id = ? " +
                "WHERE student_id = ? ";
        jdbcTemplate.update(query, newRoomId, studentId);
    }

    public void deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM rooms " +
                "WHERE room_id  = ? ";

        jdbcTemplate.update(query, id);
    }
}
