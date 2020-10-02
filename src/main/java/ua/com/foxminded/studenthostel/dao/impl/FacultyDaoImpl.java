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

    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Faculty faculty) {
        LOGGER.debug("inserting {}", faculty);

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

            LOGGER.error("insertion error {}", faculty, ex);
            throw new DaoException(faculty.toString(), ex);
        }

        return BigInteger.valueOf(keyHolder.getKey().longValue());
    }

    @Override
    public Faculty getById(BigInteger facultyId) {
        LOGGER.debug("getting by id {}", facultyId);

        String query = "" +
                "SELECT * FROM faculties " +
                "WHERE faculty_id = ? ";
        try {
            return jdbcTemplate.queryForObject(query, new FacultyMapper(), facultyId);

        } catch (EmptyResultDataAccessException ex) {

            LOGGER.warn("Failed get by id {}", facultyId, ex);
            throw new NotFoundException(facultyId.toString(), ex);
        }
    }

    @Override
    public List<Faculty> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);

        String query = "" +
                "SELECT * " +
                "FROM faculties " +
                "ORDER BY faculty_id " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, new FacultyMapper(), limit, offset);
    }

    @Override
    public BigInteger getEntriesCount() {
        LOGGER.debug("getting count of entries");

        String query = "" +
                "SELECT count(*) " +
                "FROM faculties";

        return jdbcTemplate.queryForObject(query, BigInteger.class);
    }

    @Override
    public boolean update(Faculty faculty) {
        LOGGER.debug("updating {}", faculty);

        String query = "" +
                "UPDATE faculties " +
                "SET faculty_name = ? " +
                "WHERE faculty_id = ? ";
        try {
            return jdbcTemplate.update(query, faculty.getName(), faculty.getId()) == 1;

        } catch (DataAccessException ex) {

            LOGGER.error("updating error {}", faculty);
            throw new DaoException(faculty.toString(), ex);
        }
    }

    @Override
    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        String query = "" +
                "DELETE FROM faculties " +
                "WHERE faculty_id = ? ";
        try {
            return jdbcTemplate.update(query, id) == 1;

        } catch (DataAccessException ex) {

            LOGGER.error("deleting error {}", id);
            throw new DaoException(id.toString(), ex);
        }
    }
}
