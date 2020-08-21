package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.mappers.CourseNumberMapper;

@Component
public class CourseNumberDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CourseNumberMapper courseNumberMapper;

    public void checkAndUpdate(CourseNumber courseNumber) {
        String query = "" +
                "INSERT INTO course_numbers(course_number_id, course_number_name) " +
                "VALUES (? , ? ) " +
                "ON CONFLICT ON CONSTRAINT course_numbers_pkey DO UPDATE " +
                "SET course_number_id = excluded.course_number_id, course_number_name = excluded.course_number_name;";

        jdbcTemplate.update(query, courseNumber.getId(), courseNumber.getName());
    }

    public CourseNumber getById(int courseNumberId) {
        String query = "" +
                "SELECT * FROM course_numbers " +
                "WHERE course_number_id = ?";
        return jdbcTemplate.queryForObject(query, courseNumberMapper, courseNumberId);
    }
}
