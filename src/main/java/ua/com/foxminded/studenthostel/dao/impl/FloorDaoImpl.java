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
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.mappers.FloorMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;


@Repository
public class FloorDaoImpl implements FloorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(FloorDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Floor floor) {
        LOGGER.debug("inserting {}", floor);

        String query = "" +
                "INSERT INTO floors (floor_name) " +
                "VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[]{"floor_id"});
                ps.setString(1, floor.getName());
                return ps;
            }, keyHolder);

            long id = keyHolder.getKey().longValue();
            LOGGER.debug("inserting complete, id = {}", id);
            return BigInteger.valueOf(id);

        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", floor, ex);
            throw new DaoException("Insertion error: " + floor, ex);
        }
    }

    @Override
    public Floor getById(BigInteger floorId) {
        LOGGER.debug("getting by id {}", floorId);

        String query = "" +
                "SELECT * " +
                "FROM floors " +
                "WHERE floor_id = ? ";
        try {
            Floor floor = jdbcTemplate.queryForObject(query, new FloorMapper(), floorId);
            LOGGER.debug("getting complete {}", floor);
            return floor;

        } catch (EmptyResultDataAccessException ex) {
            LOGGER.warn("Failed get by id {}", floorId, ex);
            throw new NotFoundException("Failed get by id: " + floorId, ex);
        }
    }

    @Override
    public List<Floor> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);

        String query = "" +
                "SELECT * " +
                "FROM floors " +
                "ORDER BY floor_id " +
                "LIMIT ? OFFSET ? ";

        return jdbcTemplate.query(query, new FloorMapper(), limit, offset);
    }

    @Override
    public boolean update(Floor floor) {
        LOGGER.debug("updating {}", floor);

        String query = "" +
                "UPDATE floors " +
                "SET floor_name = ? " +
                "WHERE floor_id = ? ";
        try {
            return jdbcTemplate.update(query, floor.getName(), floor.getId()) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", floor, ex);
            throw new DaoException("Updating error: " + floor, ex);
        }
    }

    @Override
    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        String query = "" +
                "DELETE FROM floors " +
                "WHERE floor_id  = ? ";
        try {
            return jdbcTemplate.update(query, id) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }
}
