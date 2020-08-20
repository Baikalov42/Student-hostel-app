package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.com.foxminded.studenthostel.models.Student;

import javax.sql.DataSource;
import java.util.List;


public class StudentDao {

    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Student> studentRowMapper = (resultSet, rowNum) -> {
        Student student = new Student();
        student.setId(resultSet.getInt("student_id"));
        student.setFirstName(resultSet.getString("first_name"));
        student.setLastName(resultSet.getString("last_name"));
        student.setHoursDebt(resultSet.getInt("hours_debt"));
        student.setGroupId(resultSet.getInt("group_id"));
        student.setRoomId(resultSet.getInt("room_id"));

        return student;
    };

    @Autowired
    public StudentDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(Student student) {
        String firstName = student.getFirstName();
        String lastName = student.getLastName();
        int groupId = student.getGroupId();
        int roomId = student.getRoomId();
        int hours = student.getHoursDebt();

        String query = "" +
                "INSERT INTO students (first_name, last_name, hours_debt, group_id, room_id) " +
                "VALUES (? , ? , ? , ? , ? )";

        jdbcTemplate.update(query, firstName, lastName, hours, groupId, roomId);
    }

    public Student getById(int studentID) {
        String query = "" +
                "SELECT * FROM students " +
                "WHERE student_id = ? ";
        return jdbcTemplate.queryForObject(query, studentRowMapper, studentID);
    }

    public List<Student> showAllByFloor(int floorId) {
        String query = "" +
                "SELECT * " +
                "FROM students" +
                "         INNER JOIN groups ON students.group_id = groups.group_id" +
                "         INNER JOIN rooms ON students.room_id = rooms.room_id " +
                "         INNER JOIN floors ON rooms.floor_id = floors.floor_id " +
                "WHERE floors.floor_id = ?;";


        return jdbcTemplate.query(query, studentRowMapper, floorId);
    }

    public List<Student> showByFaculty(int facultyId) {
        String query = "" +
                "SELECT * " +
                "FROM students" +
                "         INNER JOIN groups ON students.group_id = groups.group_id " +
                "         INNER JOIN facultys ON groups.faculty_id = facultys.faculty_id " +
                "WHERE facultys.faculty_id = ? ";

        return jdbcTemplate.query(query, studentRowMapper, facultyId);
    }

    public List<Student> showByCourse(int courseId) {
        String query = "" +
                "SELECT * " +
                "FROM students" +
                "         INNER JOIN groups ON students.group_id = groups.group_id " +
                "         INNER JOIN course_numbers ON groups.course_number_id = course_numbers.course_number_id " +
                "WHERE course_numbers.course_number_id = ? ";

        return jdbcTemplate.query(query, studentRowMapper, courseId);
    }

    public List<Student> showFromGroupWithDebit(int groupID, int numberOfHoursDebt) {
        String query = "" +
                "SELECT * FROM students " +
                "INNER JOIN groups ON groups.group_id = students.student_id " +
                "WHERE students.group_id = ? " +
                "AND students.hours_debt > ? ";
        return jdbcTemplate.query(query, studentRowMapper, groupID, numberOfHoursDebt);
    }

    public void deleteById(int studentId) {
        String query = "" +
                "DELETE  from  students " +
                "WHERE student_id = ? ";
        jdbcTemplate.update(query, studentId);
    }
}
