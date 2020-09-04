package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.mappers.FacultyMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class FacultyDaoImpl implements FacultyDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Faculty faculty) {

        String query = "" +
                "INSERT INTO faculties (faculty_name) " +
                "VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"faculty_id"});
            ps.setString(1, faculty.getName());
            return ps;
        }, keyHolder);

        return BigInteger.valueOf(keyHolder.getKey().longValue());

    }

    @Override
    public Faculty getById(BigInteger facultyId) {
        String query = "" +
                "SELECT * FROM faculties " +
                "WHERE faculty_id = ? ";
        try {
            return jdbcTemplate.queryForObject(query, new FacultyMapper(), facultyId);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("failed to get object");
        }
    }

    @Override
    public List<Faculty> getAll(long limit, long offset) {
        String query = "" +
                "SELECT * " +
                "FROM faculties " +
                "ORDER BY faculty_id " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, new FacultyMapper(), limit, offset);
    }

    @Override
    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM faculties " +
                "WHERE faculty_id = ? ";

        return jdbcTemplate.update(query, id) == 1;
    }
}
