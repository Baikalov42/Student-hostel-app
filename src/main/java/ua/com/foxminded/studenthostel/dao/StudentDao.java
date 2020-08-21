package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.mappers.StudentMapper;

import java.util.List;

@Component()
public class StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StudentMapper studentMapper;

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
        return jdbcTemplate.queryForObject(query, studentMapper, studentID);
    }

    public List<Student> showAllByFloor(int floorId) {
        String query = "" +
                "SELECT * " +
                "FROM students " +
                "INNER JOIN groups ON students.group_id = groups.group_id " +
                "INNER JOIN rooms ON students.room_id = rooms.room_id " +
                "INNER JOIN floors ON rooms.floor_id = floors.floor_id " +
                "WHERE floors.floor_id = ?;";


        return jdbcTemplate.query(query, studentMapper, floorId);
    }

    public List<Student> showByFaculty(int facultyId) {
        String query = "" +
                "SELECT * " +
                "FROM students " +
                "INNER JOIN groups ON students.group_id = groups.group_id " +
                "INNER JOIN facultys ON groups.faculty_id = facultys.faculty_id " +
                "WHERE facultys.faculty_id = ? ";

        return jdbcTemplate.query(query, studentMapper, facultyId);
    }

    public List<Student> showByCourse(int courseId) {
        String query = "" +
                "SELECT * " +
                "FROM students " +
                "INNER JOIN groups ON students.group_id = groups.group_id " +
                "INNER JOIN course_numbers ON groups.course_number_id = course_numbers.course_number_id " +
                "WHERE course_numbers.course_number_id = ? ";

        return jdbcTemplate.query(query, studentMapper, courseId);
    }

    public List<Student> showFromGroupWithDebit(int groupID, int numberOfHoursDebt) {
        String query = "" +
                "SELECT * FROM students " +
                "INNER JOIN groups ON groups.group_id = students.student_id " +
                "WHERE students.group_id = ? " +
                "AND students.hours_debt > ? ";
        return jdbcTemplate.query(query, studentMapper, groupID, numberOfHoursDebt);
    }

    public void deleteById(int studentId) {
        String query = "" +
                "DELETE  from  students " +
                "WHERE student_id = ? ";
        jdbcTemplate.update(query, studentId);
    }
}
