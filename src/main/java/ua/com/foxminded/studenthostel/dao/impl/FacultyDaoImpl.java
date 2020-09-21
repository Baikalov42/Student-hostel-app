package ua.com.foxminded.studenthostel.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
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
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[]{"faculty_id"});
                ps.setString(1, faculty.getName());
                return ps;
            }, keyHolder);

        } catch (DataAccessException ex) {
            throw new DaoException(faculty.toString(), ex);
        }
        return BigInteger.valueOf(keyHolder.getKey().longValue());

    }

    @Override
    public Faculty getById(BigInteger facultyId) {
        String query = "" +
                "SELECT * FROM faculties " +
                "WHERE faculty_id = ? ";
        try {
            return jdbcTemplate.queryForObject(query, new FacultyMapper(), facultyId);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(facultyId.toString(), ex);
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
    public BigInteger getEntriesCount() {
        String query = "" +
                "SELECT count(*) " +
                "FROM faculties";

        return jdbcTemplate.queryForObject(query, BigInteger.class);
    }

    @Override
    public boolean update(Faculty faculty) {
        String query = "" +
                "UPDATE faculties " +
                "SET faculty_name = ? " +
                "WHERE faculty_id = ? ";
        try {
            return jdbcTemplate.update(query, faculty.getName(), faculty.getId()) == 1;

        } catch (DataAccessException ex) {
            throw new DaoException(faculty.toString(), ex);
        }
    }

    @Override
    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM faculties " +
                "WHERE faculty_id = ? ";
        try {
            return jdbcTemplate.update(query, id) == 1;

        } catch (DataAccessException ex) {
            throw new DaoException(id.toString(), ex);
        }
    }
}
