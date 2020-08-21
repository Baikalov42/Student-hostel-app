package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.mappers.GroupMapper;

@Component
public class GroupDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private GroupMapper groupMapper;

    public void insert(Group group) {
        String name = group.getName();
        int facultyId = group.getFacultyId();
        int courseNumberId = group.getCourseNumberId();

        String query = "" +
                "INSERT INTO groups (group_name, faculty_id, course_number_id) " +
                "VALUES (? , ? , ? )";

        jdbcTemplate.update(query, name, facultyId, courseNumberId);
    }

    public Group getById(int groupId) {
        String query = "" +
                "SELECT * FROM groups " +
                "WHERE group_id = ? ";
        return jdbcTemplate.queryForObject(query, groupMapper, groupId);
    }
}
