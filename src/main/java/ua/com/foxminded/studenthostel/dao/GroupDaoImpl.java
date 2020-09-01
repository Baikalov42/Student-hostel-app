package ua.com.foxminded.studenthostel.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.mappers.GroupMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class GroupDaoImpl implements GroupDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Group group) {
        String query = "" +
                "INSERT INTO groups (group_name, faculty_id, course_number_id) " +
                "VALUES (? , ? , ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"group_id"});
            ps.setString(1, group.getName());
            ps.setLong(2, group.getFacultyId().longValue());
            ps.setLong(3, group.getCourseNumberId().longValue());

            return ps;
        }, keyHolder);

        return BigInteger.valueOf(keyHolder.getKey().longValue());
    }

    @Override
    public Group getById(BigInteger groupId) {
        String query = "" +
                "SELECT * FROM groups " +
                "WHERE group_id = ? ";
        try {
            return jdbcTemplate.queryForObject(query, new GroupMapper(), groupId);
        } catch (
                EmptyResultDataAccessException e) {
            throw new DaoException("failed to get object");
        }
    }

    @Override
    public List<Group> getAll(long limit, long offset) {
        String query = "" +
                "SELECT * " +
                "FROM groups " +
                "ORDER BY group_id " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, new GroupMapper(), limit, offset);
    }

    @Override
    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM groups " +
                "WHERE group_id  = ? ";

        return jdbcTemplate.update(query, id) == 1;
    }
}
