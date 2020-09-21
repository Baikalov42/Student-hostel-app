package ua.com.foxminded.studenthostel.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.mappers.CourseNumberMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CourseNumberDaoImpl implements CourseNumberDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(CourseNumber courseNumber) {
        String query = "" +
                "INSERT INTO course_numbers(course_number_name) " +
                "VALUES ( ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[]{"course_number_id"});
                ps.setString(1, courseNumber.getName());
                return ps;
            }, keyHolder);

        } catch (DataAccessException ex) {
            throw new DaoException(courseNumber.toString(), ex);
        }

        return BigInteger.valueOf(keyHolder.getKey().longValue());
    }

    @Override
    public CourseNumber getById(BigInteger courseNumberId) {
        String query = "" +
                "SELECT * FROM course_numbers " +
                "WHERE course_number_id = ?";
        try {
            return jdbcTemplate.queryForObject(query, new CourseNumberMapper(), courseNumberId);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(courseNumberId.toString(), ex);
        }
    }

    @Override
    public List<CourseNumber> getAll(long limit, long offset) {
        String query = "" +
                "SELECT * " +
                "FROM course_numbers " +
                "ORDER BY course_number_id " +
                "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(query, new CourseNumberMapper(), limit, offset);
    }

    @Override
    public BigInteger getEntriesCount() {
        String query = "" +
                "SELECT count(*) " +
                "FROM course_numbers";

        return jdbcTemplate.queryForObject(query, BigInteger.class);
    }

    @Override
    public boolean update(CourseNumber courseNumber) {
        String query = "" +
                "UPDATE course_numbers " +
                "SET course_number_name = ? " +
                "WHERE course_number_id = ? ";
        try {
            return jdbcTemplate.update(query, courseNumber.getName(), courseNumber.getId()) == 1;
        } catch (DataAccessException ex) {
            throw new DaoException(courseNumber.toString(), ex);
        }
    }

    @Override
    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM course_numbers " +
                "WHERE course_number_id = ? ";

        try {
            return jdbcTemplate.update(query, id) == 1;
        } catch (DataAccessException ex) {
            throw new DaoException(id.toString(), ex);
        }
    }
}