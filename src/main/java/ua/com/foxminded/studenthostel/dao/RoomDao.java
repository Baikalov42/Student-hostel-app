package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.mappers.RoomMapper;

import java.util.List;

@Component
public class RoomDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RoomMapper roomMapper;

    public void insert(Room room) {

        String query = "" +
                "INSERT INTO rooms (room_name, floor_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(query, room.getName(), room.getFloorId());
    }

    public Room getById(int roomId) {
        String query = "" +
                "SELECT * " +
                "FROM rooms " +
                "WHERE room_id = ? ";
        return jdbcTemplate.queryForObject(query, roomMapper, roomId);
    }

    public List<Room> getAllByEquipment(int equipmentId) {
        String query = "" +
                "SELECT * FROM equipments " +
                "INNER JOIN students_equipments ON equipments.equipments_id = students_equipments.equipment_id " +
                "INNER JOIN students ON students_equipments.student_id = students.student_id " +
                "INNER JOIN rooms ON students.room_id = rooms.room_id " +
                "WHERE equipments_id = ? ";
        return jdbcTemplate.query(query, roomMapper, equipmentId);
    }

    public void changeRoom(int newRoomId, int studentID) {
        String query = "" +
                "UPDATE students " +
                "SET room_id = ? " +
                "WHERE student_id = ? ";
        jdbcTemplate.update(query, newRoomId, studentID);
    }
}
