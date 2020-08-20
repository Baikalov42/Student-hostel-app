package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.com.foxminded.studenthostel.models.Room;

import javax.sql.DataSource;
import java.util.List;

public class RoomDao {

    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Room> roomRowMapper = (resultSet, rowNum) -> {

        Room room = new Room();
        room.setName(resultSet.getString("room_name"));
        room.setFloorId(resultSet.getInt("floor_id"));
        room.setId(resultSet.getInt("room_id"));

        return room;
    };

    @Autowired
    public RoomDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Room getById(int roomId) {
        String query = "" +
                "SELECT * FROM rooms " +
                "WHERE room_id = ? ";
        return jdbcTemplate.queryForObject(query, roomRowMapper, roomId);
    }

    public List<Room> getAllByEquipment(int equipmentId) {
        String query = "" +
                "SELECT * FROM equipments " +
                "   INNER JOIN students_equipments ON equipments.equipments_id = students_equipments.equipment_id " +
                "   INNER JOIN students ON students_equipments.student_id = students.student_id " +
                "   INNER JOIN rooms ON students.room_id = rooms.room_id " +
                "WHERE equipments_id = ? ";
        return jdbcTemplate.query(query, roomRowMapper, equipmentId);
    }

    public void changeRoom(int newRoomId, int studentID) {
        String query = "" +
                "UPDATE students " +
                "SET room_id = ? " +
                "WHERE student_id = ? ";
        jdbcTemplate.update(query, newRoomId, studentID);
    }
}
