package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.mappers.StudentMapper;

import java.math.BigInteger;
import java.util.List;

@Component
public class  StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(Student student) {
        String firstName = student.getFirstName();
        String lastName = student.getLastName();
        BigInteger groupId = student.getGroupId();
        BigInteger roomId = student.getRoomId();
        int hours = student.getHoursDebt();

        String query = "" +
                "INSERT INTO students (first_name, last_name, hours_debt, group_id, room_id) " +
                "VALUES (? , ? , ? , ? , ? )";

        jdbcTemplate.update(query, firstName, lastName, hours, groupId, roomId);
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

    public void deleteById(BigInteger id) {
        String query = "" +
                "DELETE  from  students " +
                "WHERE student_id = ? ";
        jdbcTemplate.update(query, id);
    }
}
