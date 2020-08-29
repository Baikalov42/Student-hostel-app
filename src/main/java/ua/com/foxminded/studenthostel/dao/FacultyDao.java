package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.mappers.FacultyMapper;

import java.math.BigInteger;


@Component
public class FacultyDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Faculty faculty) {
        String query = "" +
                "INSERT INTO faculties (faculty_id, faculty_name) " +
                "VALUES (? , ? ) ";

        jdbcTemplate.update(query, faculty.getId(), faculty.getName());
    }

    public Faculty getById(BigInteger facultyId) {
        String query = "" +
                "SELECT * FROM faculties " +
                "WHERE faculty_id = ? ";

        return jdbcTemplate.queryForObject(query, new FacultyMapper(), facultyId);
    }
}
