package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.mappers.FloorMapper;

import java.math.BigInteger;

@Component
public class FloorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(Floor floor) {
        String query = "" +
                "INSERT INTO floors (floor_name) " +
                "VALUES (?)";

        jdbcTemplate.update(query, floor.getName());
    }

    public Floor getById(BigInteger floorId) {
        String query = "" +
                "SELECT * " +
                "FROM floors " +
                "WHERE floor_id = ? ";

        return jdbcTemplate.queryForObject(query, new FloorMapper(), floorId);
    }

    public void deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM floors " +
                "WHERE floor_id  = ? ";

        jdbcTemplate.update(query, id);
    }
}
