package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.mappers.FacultyMapper;


@Component
public class FacultyDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FacultyMapper facultyMapper;

    public void checkAndUpdate(Faculty faculty) {
        String query = "" +
                "INSERT INTO faculties (faculty_id, faculty_name) " +
                "VALUES (? , ? ) " +
                "ON CONFLICT ON CONSTRAINT faculties_pkey DO UPDATE " +
                "SET faculty_id = excluded.faculty_id, faculty_name = excluded.faculty_name;";

        jdbcTemplate.update(query, faculty.getId(), faculty.getName());
    }

    public Faculty getById(int facultyId) {
        String query = "" +
                "SELECT * FROM faculties " +
                "WHERE faculty_id = ? ";

        return jdbcTemplate.queryForObject(query, facultyMapper, facultyId);
    }
}
