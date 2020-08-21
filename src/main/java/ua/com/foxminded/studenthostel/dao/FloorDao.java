package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.mappers.FloorMapper;

@Component
public class FloorDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FloorMapper floorMapper;

    public void insert(Floor floor) {
        String query = "" +
                "INSERT INTO floors (floor_name) " +
                "VALUES (?)";

        jdbcTemplate.update(query, floor.getName() );
    }

    public Floor getById(int floorId) {
        String query = "" +
                "SELECT * " +
                "FROM floors " +
                "WHERE floor_id = ? ";

        return jdbcTemplate.queryForObject(query, floorMapper, floorId);
    }
}
