package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.mappers.CourseNumberMapper;

import java.math.BigInteger;
import java.util.List;

@Repository
public class CourseNumberDaoImpl implements CourseNumberDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(CourseNumber courseNumber) {
        String query = "" +
                "INSERT INTO course_numbers(course_number_id, course_number_name) " +
                "VALUES (? , ? ) " +
                "ON CONFLICT (course_number_id) DO NOTHING";

        jdbcTemplate.update(query, courseNumber.getId(), courseNumber.getName());
    }

    @Override
    public CourseNumber getById(BigInteger courseNumberId) {
        String query = "" +
                "SELECT * FROM course_numbers " +
                "WHERE course_number_id = ?";
        try {
            return jdbcTemplate.queryForObject(query, new CourseNumberMapper(), courseNumberId);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("failed to get object");
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
}
