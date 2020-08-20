package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.com.foxminded.studenthostel.models.Floor;

import javax.sql.DataSource;

public class FloorDao {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<Floor> floorRowMapper = (resultSet, rowNum) -> {

        Floor floor = new Floor();
        floor.setId(resultSet.getInt("floor_id"));
        floor.setName(resultSet.getString("floor_name"));

        return floor;
    };

    @Autowired
    public FloorDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Floor getById(int floorId) {
        String query = "" +
                "SELECT * FROM floors " +
                "WHERE floor_id = ? ";

        return jdbcTemplate.queryForObject(query, floorRowMapper, floorId);
    }

}
