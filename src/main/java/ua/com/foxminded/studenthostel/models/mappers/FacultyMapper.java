package ua.com.foxminded.studenthostel.models.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Faculty;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FacultyMapper implements RowMapper<Faculty> {

    @Override
    public Faculty mapRow(ResultSet resultSet, int i) throws SQLException {

        String facultyName = resultSet.getString("faculty_name");
        return Faculty.getByName(facultyName);
    }
}
