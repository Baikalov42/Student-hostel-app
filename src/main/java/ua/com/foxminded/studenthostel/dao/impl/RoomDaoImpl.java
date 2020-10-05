package ua.com.foxminded.studenthostel.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.dao.RoomDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.mappers.RoomMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class RoomDaoImpl implements RoomDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Room room) {
        LOGGER.debug("inserting {}", room);

        String query = "" +
                "INSERT INTO rooms (room_name, floor_id) " +
                "VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[]{"room_id"});
                ps.setString(1, room.getName());
                ps.setLong(2, room.getFloorId().longValue());

                return ps;
            }, keyHolder);

            long id = keyHolder.getKey().longValue();
            LOGGER.debug("inserting complete, id = {}", id);
            return BigInteger.valueOf(id);

        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", room, ex);
            throw new DaoException(room.toString(), ex);
        }
    }

    @Override
    public Room getById(BigInteger roomId) {
        LOGGER.debug("getting by id {}", roomId);

        String query = "" +
                "SELECT * " +
                "FROM rooms " +
                "WHERE room_id = ? ";
        try {
            Room room = jdbcTemplate.queryForObject(query, new RoomMapper(), roomId);
            LOGGER.debug("getting complete {}", room);
            return room;

        } catch (EmptyResultDataAccessException ex) {
            LOGGER.warn("Failed get by id {}", roomId, ex);
            throw new NotFoundException(roomId.toString(), ex);
        }
    }

    @Override
    public List<Room> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);

        String query = "" +
                "SELECT * " +
                "FROM rooms " +
                "ORDER BY room_id " +
                "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(query, new RoomMapper(), limit, offset);
    }

    @Override
    public List<Room> getAllByEquipment(BigInteger equipmentId) {
        LOGGER.debug("getting all by Equipment id {} ", equipmentId);

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
        LOGGER.debug("updating {}", room);

        String query = "" +
                "UPDATE rooms " +
                "SET " +
                "room_name = ? ," +
                "floor_id = ? " +
                "WHERE room_id = ? ";
        try {
            return jdbcTemplate.update(query, room.getName(), room.getFloorId(), room.getId()) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", room, ex);
            throw new DaoException(room.toString(), ex);
        }
    }

    @Override
    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        String query = "" +
                "DELETE FROM rooms " +
                "WHERE room_id  = ? ";
        try {
            return jdbcTemplate.update(query, id) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException(id.toString(), ex);
        }
    }
}
