package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.mappers.GroupMapper;

import java.math.BigInteger;

@Component
public class GroupDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(Group group) {
        String name = group.getName();
        BigInteger facultyId = group.getFacultyId();
        BigInteger courseNumberId = group.getCourseNumberId();

        String query = "" +
                "INSERT INTO groups (group_name, faculty_id, course_number_id) " +
                "VALUES (? , ? , ? )";

        jdbcTemplate.update(query, name, facultyId, courseNumberId);
    }

    public Group getById(BigInteger groupId) {
        String query = "" +
                "SELECT * FROM groups " +
                "WHERE group_id = ? ";
        return jdbcTemplate.queryForObject(query, new GroupMapper(), groupId);
    }

    public void deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM groups " +
                "WHERE group_id  = ? ";

        jdbcTemplate.update(query, id);
    }
}
