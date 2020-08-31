package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.mappers.FloorMapper;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Component
public class FloorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Qualifier("floorJdbcInsert")
    @Autowired()
    private SimpleJdbcInsert floorJdbcInsert;

    public BigInteger insert(Floor floor) {

        Map<String, Object> map = new HashMap<>();
        map.put("floor_name", floor.getName());

        return BigInteger.valueOf(floorJdbcInsert.executeAndReturnKey(map).longValue());
    }

    public Floor getById(BigInteger floorId) {
        String query = "" +
                "SELECT * " +
                "FROM floors " +
                "WHERE floor_id = ? ";

        return jdbcTemplate.queryForObject(query, new FloorMapper(), floorId);
    }

    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM floors " +
                "WHERE floor_id  = ? ";

        return jdbcTemplate.update(query, id) == 1;
    }
}
