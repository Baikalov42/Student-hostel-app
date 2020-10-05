package ua.com.foxminded.studenthostel.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.dao.StudentDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.mappers.StudentMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class StudentDaoImpl implements StudentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Student student) {
        LOGGER.debug("inserting {}", student);

        String query = "" +
                "INSERT INTO students (first_name, last_name, hours_debt, group_id, room_id) " +
                "VALUES (? , ? , ? , ? , ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[]{"student_id"});
                ps.setString(1, student.getFirstName());
                ps.setString(2, student.getLastName());
                ps.setInt(3, student.getHoursDebt());
                ps.setLong(4, student.getGroupId().longValue());
                ps.setLong(5, student.getRoomId().longValue());

                return ps;
            }, keyHolder);

            long id = keyHolder.getKey().longValue();
            LOGGER.debug("inserting complete, id = {}", id);
            return BigInteger.valueOf(id);

        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", student, ex);
            throw new DaoException(student.toString(), ex);
        }
    }

    @Override
    public Student getById(BigInteger studentId) {
        LOGGER.debug("getting by id {}", studentId);

        String query = "" +
                "SELECT * FROM students " +
                "WHERE student_id = ? ";
        try {
            Student student = jdbcTemplate.queryForObject(query, new StudentMapper(), studentId);
            LOGGER.debug("getting complete {}", student);
            return student;

        } catch (EmptyResultDataAccessException ex) {
            LOGGER.warn("Failed get by id {}", studentId, ex);
            throw new NotFoundException(studentId.toString(), ex);
        }
    }

    @Override
    public List<Student> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);

        String query = "" +
                "SELECT * " +
                "FROM students " +
                "ORDER BY student_id " +
                "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(query, new StudentMapper(), limit, offset);
    }

    @Override
    public List<Student> getAllByFloor(BigInteger floorId) {
        LOGGER.debug("getting all by floor id {} ", floorId);

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
        LOGGER.debug("getting all by faculty id {} ", facultyId);

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
        LOGGER.debug("getting all by course id {} ", courseId);

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
        LOGGER.debug("getting all by Group id ={} , with debt = {} ", groupId, numberOfHoursDebt);

        String query = "" +
                "SELECT * FROM students " +
                "INNER JOIN groups ON groups.group_id = students.student_id " +
                "WHERE students.group_id = ? " +
                "AND students.hours_debt > ? ";
        return jdbcTemplate.query(query, new StudentMapper(), groupId, numberOfHoursDebt);
    }

    @Override
    public boolean changeRoom(BigInteger newRoomId, BigInteger studentId) {
        LOGGER.debug("changing room id = {}, student id = {}", newRoomId, studentId);

        String query = "" +
                "UPDATE students " +
                "SET room_id = ? " +
                "WHERE student_id = ? ";
        try {
            return jdbcTemplate.update(query, newRoomId, studentId) == 1;
        } catch (DataAccessException ex) {

            LOGGER.error("failed to change new room id = {}, new student id = {}", newRoomId, studentId);
            throw new DaoException("room id=" + newRoomId + " student id=" + studentId, ex);
        }
    }

    @Override
    public boolean changeDebt(int newHoursDebt, BigInteger studentId) {
        LOGGER.debug("changing debt, new debt = {}, student id = {}", newHoursDebt, studentId);

        String query = "" +
                "UPDATE students " +
                "SET hours_debt = ? " +
                "WHERE student_id = ? ";
        try {
            return jdbcTemplate.update(query, newHoursDebt, studentId) == 1;
        } catch (DataAccessException ex) {

            LOGGER.error("failed to change debt, new debt = {}, student id = {}", newHoursDebt, studentId);
            throw new DaoException("new hours=" + newHoursDebt + " student id=" + studentId, ex);
        }
    }

    @Override
    public Integer getStudentsCountByRoom(BigInteger roomID) {
        LOGGER.debug("getting count of students, by room id {}", roomID);

        String query = "" +
                "SELECT count(*) " +
                "FROM students " +
                "WHERE room_id = ?";

        int count = jdbcTemplate.queryForObject(query, Integer.class, roomID);
        LOGGER.debug("getting count of students complete, count = {}", count);

        return count;
    }

    @Override
    public boolean update(Student student) {
        LOGGER.debug("updating {}", student);

        String query = "" +
                "UPDATE students " +
                "SET " +
                "first_name = ? ," +
                "last_name = ? ," +
                "hours_debt = ? ," +
                "group_id = ? ," +
                "room_id = ? " +
                "WHERE student_id = ? ";

        try {
            return jdbcTemplate.update(query, student.getFirstName(), student.getLastName(),
                    student.getHoursDebt(), student.getGroupId(), student.getRoomId(), student.getId()) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", student, ex);
            throw new DaoException(student.toString(), ex);
        }
    }

    @Override
    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        String query = "" +
                "DELETE  from  students " +
                "WHERE student_id = ? ";
        try {
            return jdbcTemplate.update(query, id) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException(id.toString(), ex);
        }
    }
}
