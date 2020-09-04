package ua.com.foxminded.studenthostel.models.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Faculty;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FacultyMapper implements RowMapper<Faculty> {

    @Override
    public Faculty mapRow(ResultSet resultSet, int i) throws SQLException {

        Faculty faculty = new Faculty();

        faculty.setId(BigInteger.valueOf(resultSet.getLong("faculty_id")));
        faculty.setName(resultSet.getString("faculty_name"));

        return faculty;
    }
}
