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

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Group group) {
        LOGGER.debug("inserting {}", group);

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

            long id = keyHolder.getKey().longValue();
            LOGGER.debug("inserting complete, id = {}", id);
            return BigInteger.valueOf(id);

        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", group, ex);
            throw new DaoException(group.toString(), ex);
        }
    }

    @Override
    public Group getById(BigInteger groupId) {
        LOGGER.debug("getting by id {}", groupId);

        String query = "" +
                "SELECT * FROM groups " +
                "WHERE group_id = ? ";
        try {
            Group group = jdbcTemplate.queryForObject(query, new GroupMapper(), groupId);
            LOGGER.debug("getting complete {}", group);
            return group;

        } catch (EmptyResultDataAccessException ex) {
            LOGGER.warn("Failed get by id {}", groupId, ex);
            throw new NotFoundException(groupId.toString(), ex);
        }
    }

    @Override
    public List<Group> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);

        String query = "" +
                "SELECT * " +
                "FROM groups " +
                "ORDER BY group_id " +
                "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(query, new GroupMapper(), limit, offset);
    }

    @Override
    public boolean update(Group group) {
        LOGGER.debug("updating {}", group);

        String query = "" +
                "UPDATE groups " +
                "SET group_name = ?" +
                "WHERE group_id = ?";
        try {
            return jdbcTemplate.update(query, group.getName(), group.getId()) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", group, ex);
            throw new DaoException(group.toString(), ex);
        }
    }

    @Override
    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        String query = "" +
                "DELETE FROM groups " +
                "WHERE group_id  = ? ";
        try {
            return jdbcTemplate.update(query, id) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException(id.toString(), ex);
        }
    }
}
