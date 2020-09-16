package ua.com.foxminded.studenthostel.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.mappers.FloorMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;


@Repository
public class FloorDaoImpl implements FloorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Floor floor) {
        String query = "" +
                "INSERT INTO floors (floor_name) " +
                "VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"floor_id"});
            ps.setString(1, floor.getName());
            return ps;
        }, keyHolder);

        return BigInteger.valueOf(keyHolder.getKey().longValue());
    }

    @Override
    public Floor getById(BigInteger floorId) {
        String query = "" +
                "SELECT * " +
                "FROM floors " +
                "WHERE floor_id = ? ";
        try {
            return jdbcTemplate.queryForObject(query, new FloorMapper(), floorId);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("failed to get object");
        }
    }

    @Override
    public List<Floor> getAll(long limit, long offset) {
        String query = "" +
                "SELECT * " +
                "FROM floors " +
                "ORDER BY floor_id " +
                "LIMIT ? OFFSET ? ";
        return jdbcTemplate.query(query, new FloorMapper(), limit, offset);
    }

    @Override
    public BigInteger getEntriesCount() {
        String query = "" +
                "SELECT count(*) " +
                "FROM floors";

        return jdbcTemplate.queryForObject(query, BigInteger.class);
    }

    @Override
    public boolean update(Floor floor) {
        String query = "" +
                "UPDATE floors " +
                "SET floor_name = ? " +
                "WHERE floor_id = ? ";

        return jdbcTemplate.update(query, floor.getName(), floor.getId()) == 1;
    }

    @Override
    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM floors " +
                "WHERE floor_id  = ? ";

        return jdbcTemplate.update(query, id) == 1;
    }
}
