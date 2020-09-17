package ua.com.foxminded.studenthostel.dao.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.dao.GroupDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
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
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[]{"group_id"});
                ps.setString(1, group.getName());
                ps.setLong(2, group.getFacultyId().longValue());
                ps.setLong(3, group.getCourseNumberId().longValue());

                return ps;
            }, keyHolder);

        } catch (DataAccessException ex) {
            throw new DaoException(group.toString(), ex);
        }

        return BigInteger.valueOf(keyHolder.getKey().longValue());
    }

    @Override
    public Group getById(BigInteger groupId) {
        String query = "" +
                "SELECT * FROM groups " +
                "WHERE group_id = ? ";
        try {
            return jdbcTemplate.queryForObject(query, new GroupMapper(), groupId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(groupId.toString(), e);
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
    public boolean update(Group group) {
        String query = "" +
                "UPDATE groups " +
                "SET group_name = ?" +
                "WHERE group_id = ?";
        try {
            return jdbcTemplate.update(query, group.getName(), group.getId()) == 1;
        } catch (DataAccessException ex) {
            throw new DaoException(group.toString(), ex);
        }
    }

    @Override
    public BigInteger getEntriesCount() {
        String query = "" +
                "SELECT count(*) " +
                "FROM groups";

        return jdbcTemplate.queryForObject(query, BigInteger.class);
    }

    @Override
    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM groups " +
                "WHERE group_id  = ? ";
        try {
            return jdbcTemplate.update(query, id) == 1;

        } catch (DataAccessException ex) {
            throw new DaoException(id.toString(), ex);
        }
    }
}
