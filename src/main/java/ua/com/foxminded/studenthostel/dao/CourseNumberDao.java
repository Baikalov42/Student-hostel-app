package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.mappers.CourseNumberMapper;

import java.math.BigInteger;

@Component
public class CourseNumberDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(CourseNumber courseNumber) {
        String query = "" +
                "INSERT INTO course_numbers(course_number_id, course_number_name) " +
                "VALUES (? , ? ) ";

        jdbcTemplate.update(query, courseNumber.getId(), courseNumber.getName());
    }

    public CourseNumber getById(BigInteger courseNumberId) {
        String query = "" +
                "SELECT * FROM course_numbers " +
                "WHERE course_number_id = ?";
        return jdbcTemplate.queryForObject(query, new CourseNumberMapper(), courseNumberId);
    }
}
