package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.mappers.GroupMapper;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Component
public class GroupDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Qualifier("groupJdbcInsert")
    @Autowired
    private SimpleJdbcInsert groupJdbcInsert;

    public BigInteger insert(Group group) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("group_name", group.getName());
        parameters.put("faculty_id", group.getFacultyId());
        parameters.put("course_number_id", group.getCourseNumberId());

        return BigInteger.valueOf(groupJdbcInsert.executeAndReturnKey(parameters).longValue());
    }

    public Group getById(BigInteger groupId) {
        String query = "" +
                "SELECT * FROM groups " +
                "WHERE group_id = ? ";
        return jdbcTemplate.queryForObject(query, new GroupMapper(), groupId);
    }

    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM groups " +
                "WHERE group_id  = ? ";

        return jdbcTemplate.update(query, id) == 1;
    }
}
