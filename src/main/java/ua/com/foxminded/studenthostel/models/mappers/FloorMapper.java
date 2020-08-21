package ua.com.foxminded.studenthostel.models.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Floor;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FloorMapper implements RowMapper<Floor> {

    @Override
    public Floor mapRow(ResultSet resultSet, int i) throws SQLException {
        Floor floor = new Floor();
        floor.setId(resultSet.getInt("floor_id"));
        floor.setName(resultSet.getString("floor_name"));

        return floor;
    }
}
