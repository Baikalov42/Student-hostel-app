package ua.com.foxminded.studenthostel.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.mappers.StudentMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Student student) {
        String query = "" +
                "INSERT INTO students (first_name, last_name, hours_debt, group_id, room_id) " +
                "VALUES (? , ? , ? , ? , ? )";


        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"student_id"});
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setInt(3, student.getHoursDebt());
            ps.setLong(4, student.getGroupId().longValue());
            ps.setLong(5, student.getRoomId().longValue());

            return ps;
        }, keyHolder);

        return BigInteger.valueOf(keyHolder.getKey().longValue());
    }

    @Override
    public Student getById(BigInteger studentId) {
        String query = "" +
                "SELECT * FROM students " +
                "WHERE student_id = ? ";
        try {
            return jdbcTemplate.queryForObject(query, new StudentMapper(), studentId);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("failed to get object");
        }
    }

    @Override
    public List<Student> getAll(long limit, long offset) {
        String query = "" +
                "SELECT * " +
                "FROM students " +
                "ORDER BY student_id " +
                "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(query, new StudentMapper(), limit, offset);
    }

    @Override
    public List<Student> getAllByFloor(BigInteger floorId) {
        String query = "" +
                "SELECT * " +
                "FROM students " +
                "INNER JOIN groups ON students.group_id = groups.group_id " +
                "INNER JOIN rooms ON students.room_id = rooms.room_id " +
                "INNER JOIN floors ON rooms.floor_id = floors.floor_id " +
                "WHERE floors.floor_id = ?;";


        return jdbcTemplate.query(query, new StudentMapper(), floorId);
    }

    @Override
    public List<Student> getAllByFaculty(BigInteger facultyId) {
        String query = "" +
                "SELECT * " +
                "FROM students " +
                "INNER JOIN groups ON students.group_id = groups.group_id " +
                "INNER JOIN faculties ON groups.faculty_id = faculties.faculty_id " +
                "WHERE faculties.faculty_id = ? ";

        return jdbcTemplate.query(query, new StudentMapper(), facultyId);
    }

    @Override
    public List<Student> getAllByCourse(BigInteger courseId) {
        String query = "" +
                "SELECT * " +
                "FROM students " +
                "INNER JOIN groups ON students.group_id = groups.group_id " +
                "INNER JOIN course_numbers ON groups.course_number_id = course_numbers.course_number_id " +
                "WHERE course_numbers.course_number_id = ? ";

        return jdbcTemplate.query(query, new StudentMapper(), courseId);
    }

    @Override
    public List<Student> getAllWithDebitByGroup(BigInteger groupId, int numberOfHoursDebt) {
        String query = "" +
                "SELECT * FROM students " +
                "INNER JOIN groups ON groups.group_id = students.student_id " +
                "WHERE students.group_id = ? " +
                "AND students.hours_debt > ? ";
        return jdbcTemplate.query(query, new StudentMapper(), groupId, numberOfHoursDebt);
    }

    @Override
    public boolean changeRoom(BigInteger newRoomId, BigInteger studentId) {
        String query = "" +
                "UPDATE students " +
                "SET room_id = ? " +
                "WHERE student_id = ? ";
        return jdbcTemplate.update(query, newRoomId, studentId) == 1;
    }

    @Override
    public boolean changeDebt(int newHoursDebt, BigInteger studentId) {
        String query = "" +
                "UPDATE students " +
                "SET hours_debt = ? " +
                "WHERE student_id = ? ";
        return jdbcTemplate.update(query, newHoursDebt, studentId) == 1;
    }

    @Override
    public BigInteger getEntriesCount() {
        String query = "" +
                "SELECT count(*) " +
                "FROM students";

        return jdbcTemplate.queryForObject(query, BigInteger.class);
    }

    @Override
    public Integer getStudentsCountByRoom(BigInteger roomID) {
        String query = "" +
                "SELECT count(*) " +
                "FROM students " +
                "WHERE room_id = ?";

        return jdbcTemplate.queryForObject(query, Integer.class, roomID);
    }

    @Override
    public boolean update(Student student) {
        String query = "" +
                "UPDATE students " +
                "SET " +
                "first_name = ? ," +
                "last_name = ? ," +
                "hours_debt = ? ," +
                "group_id = ? ," +
                "room_id = ? " +
                "WHERE student_id = ? ";

        return jdbcTemplate.update(query, student.getFirstName(), student.getLastName(),
                student.getHoursDebt(), student.getGroupId(), student.getRoomId(), student.getId()) == 1;
    }

    @Override
    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE  from  students " +
                "WHERE student_id = ? ";
        return jdbcTemplate.update(query, id) == 1;
    }
}
