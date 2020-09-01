package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.mappers.FacultyMapper;

import java.math.BigInteger;
import java.util.List;

@Repository
public class FacultyDaoImpl implements FacultyDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(Faculty faculty) {
        String query = "" +
                "INSERT INTO faculties (faculty_id, faculty_name) " +
                "VALUES (? , ? ) " +
                "ON CONFLICT (faculty_id) DO NOTHING ";

        jdbcTemplate.update(query, faculty.getId(), faculty.getName());
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
}
