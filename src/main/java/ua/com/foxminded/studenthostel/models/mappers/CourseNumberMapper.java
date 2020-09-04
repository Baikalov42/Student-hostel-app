package ua.com.foxminded.studenthostel.models.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.CourseNumber;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CourseNumberMapper implements RowMapper<CourseNumber> {

    @Override
    public CourseNumber mapRow(ResultSet resultSet, int i) throws SQLException {

        CourseNumber courseNumber = new CourseNumber();

        courseNumber.setId(BigInteger.valueOf(resultSet.getLong("course_number_id")));
        courseNumber.setName(resultSet.getString("course_number_name"));

        return courseNumber;
    }
}
