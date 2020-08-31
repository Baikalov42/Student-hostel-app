package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.mappers.StudentMapper;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Qualifier("studentJdbcInsert")
    @Autowired
    private SimpleJdbcInsert studentJdbcInsert;

    public BigInteger insert(Student student) {

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("first_name", student.getFirstName());
        parameters.put("last_name", student.getLastName());
        parameters.put("hours_debt", student.getHoursDebt());
        parameters.put("group_id", student.getGroupId());
        parameters.put("room_id", student.getRoomId());

        return BigInteger.valueOf(studentJdbcInsert.executeAndReturnKey(parameters).longValue());
    }

    public Student getById(BigInteger studentId) {
        String query = "" +
                "SELECT * FROM students " +
                "WHERE student_id = ? ";
        return jdbcTemplate.queryForObject(query, new StudentMapper(), studentId);
    }

    public List<Student> showAllByFloor(BigInteger floorId) {
        String query = "" +
                "SELECT * " +
                "FROM students " +
                "INNER JOIN groups ON students.group_id = groups.group_id " +
                "INNER JOIN rooms ON students.room_id = rooms.room_id " +
                "INNER JOIN floors ON rooms.floor_id = floors.floor_id " +
                "WHERE floors.floor_id = ?;";


        return jdbcTemplate.query(query, new StudentMapper(), floorId);
    }

    public List<Student> showByFaculty(BigInteger facultyId) {
        String query = "" +
                "SELECT * " +
                "FROM students " +
                "INNER JOIN groups ON students.group_id = groups.group_id " +
                "INNER JOIN faculties ON groups.faculty_id = faculties.faculty_id " +
                "WHERE faculties.faculty_id = ? ";

        return jdbcTemplate.query(query, new StudentMapper(), facultyId);
    }

    public List<Student> showByCourse(BigInteger courseId) {
        String query = "" +
                "SELECT * " +
                "FROM students " +
                "INNER JOIN groups ON students.group_id = groups.group_id " +
                "INNER JOIN course_numbers ON groups.course_number_id = course_numbers.course_number_id " +
                "WHERE course_numbers.course_number_id = ? ";

        return jdbcTemplate.query(query, new StudentMapper(), courseId);
    }

    public List<Student> showFromGroupWithDebit(BigInteger groupId, int numberOfHoursDebt) {
        String query = "" +
                "SELECT * FROM students " +
                "INNER JOIN groups ON groups.group_id = students.student_id " +
                "WHERE students.group_id = ? " +
                "AND students.hours_debt > ? ";
        return jdbcTemplate.query(query, new StudentMapper(), groupId, numberOfHoursDebt);
    }

    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE  from  students " +
                "WHERE student_id = ? ";
        return jdbcTemplate.update(query, id) == 1;
    }
}
