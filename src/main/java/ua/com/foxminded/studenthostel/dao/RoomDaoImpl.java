package ua.com.foxminded.studenthostel.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.mappers.RoomMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class RoomDaoImpl implements RoomDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Room room) {
        String query = "" +
                "INSERT INTO rooms (room_name, floor_id) " +
                "VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"room_id"});
            ps.setString(1, room.getName());
            ps.setLong(2, room.getFloorId().longValue());

            return ps;
        }, keyHolder);

        return BigInteger.valueOf(keyHolder.getKey().longValue());
    }

    @Override
    public Room getById(BigInteger roomId) {
        String query = "" +
                "SELECT * " +
                "FROM rooms " +
                "WHERE room_id = ? ";
        try {
            return jdbcTemplate.queryForObject(query, new RoomMapper(), roomId);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("failed to get object");
        }
    }

    @Override
    public List<Room> getAll(long limit, long offset) {
        String query = "" +
                "SELECT * " +
                "FROM rooms " +
                "ORDER BY room_id " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, new RoomMapper(), limit, offset);
    }

    @Override
    public List<Room> getAllByEquipment(BigInteger equipmentId) {
        String query = "" +
                "SELECT * FROM equipments " +
                "INNER JOIN students_equipments ON equipments.equipment_id = students_equipments.equipment_id " +
                "INNER JOIN students ON students_equipments.student_id = students.student_id " +
                "INNER JOIN rooms ON students.room_id = rooms.room_id " +
                "WHERE equipments.equipment_id = ? ";
        return jdbcTemplate.query(query, new RoomMapper(), equipmentId);
    }

    @Override
    public boolean update(Room room) {
        String query = "" +
                "UPDATE rooms " +
                "SET " +
                "room_name = ? ," +
                "floor_id = ? " +
                "WHERE room_id = ? ";

        return jdbcTemplate.update(query, room.getName(), room.getFloorId(), room.getId()) == 1;
    }

    @Override
    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM rooms " +
                "WHERE room_id  = ? ";

        return jdbcTemplate.update(query, id) == 1;
    }
}
