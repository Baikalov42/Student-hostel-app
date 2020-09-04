package ua.com.foxminded.studenthostel.models.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Student;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {

        Student student = new Student();

        student.setId(BigInteger.valueOf(resultSet.getLong("student_id")));
        student.setFirstName(resultSet.getString("first_name"));
        student.setLastName(resultSet.getString("last_name"));
        student.setHoursDebt(resultSet.getInt("hours_debt"));
        student.setGroupId(BigInteger.valueOf(resultSet.getLong("group_id")));
        student.setRoomId(BigInteger.valueOf(resultSet.getLong("room_id")));

        return student;
    }
}
