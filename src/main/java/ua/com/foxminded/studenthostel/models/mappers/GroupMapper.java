package ua.com.foxminded.studenthostel.models.mappers;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Group;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet resultSet, int i) throws SQLException {
        Group group = new Group();

        group.setName(resultSet.getString("group_name"));
        group.setId(BigInteger.valueOf(resultSet.getInt("group_id")));
        group.setCourseNumberId(BigInteger.valueOf(resultSet.getInt("course_number_id")));
        group.setFacultyId(BigInteger.valueOf(resultSet.getInt("faculty_id")));

        return group;
    }
}
