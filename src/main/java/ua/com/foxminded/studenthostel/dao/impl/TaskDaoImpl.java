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
import ua.com.foxminded.studenthostel.dao.TaskDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.models.mappers.TaskMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TaskDaoImpl implements TaskDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Task task) {
        LOGGER.debug("inserting {}", task);

        String query = "" +
                "INSERT INTO tasks (task_name, task_description, cost) " +
                "VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[]{"task_id"});
                ps.setString(1, task.getName());
                ps.setString(2, task.getDescription());
                ps.setInt(3, task.getCostInHours());

                return ps;
            }, keyHolder);

            long id = keyHolder.getKey().longValue();
            LOGGER.debug("inserting complete, id = {}", id);
            return BigInteger.valueOf(id);

        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", task, ex);
            throw new DaoException(task.toString(), ex);
        }
    }

    @Override
    public Task getById(BigInteger taskId) {
        LOGGER.debug("getting by id {}", taskId);

        String query = "" +
                "SELECT * " +
                "FROM tasks " +
                "WHERE task_id = ? ";
        try {
            Task task = jdbcTemplate.queryForObject(query, new TaskMapper(), taskId);
            LOGGER.debug("getting complete {}", task);
            return task;

        } catch (EmptyResultDataAccessException ex) {
            LOGGER.warn("Failed get by id {}", taskId, ex);
            throw new NotFoundException(taskId.toString(), ex);
        }
    }

    @Override
    public List<Task> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);

        String query = "" +
                "SELECT * " +
                "FROM tasks " +
                "ORDER BY task_id " +
                "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(query, new TaskMapper(), limit, offset);
    }

    @Override
    public List<Task> getAllByStudent(BigInteger studentId) {
        LOGGER.debug("getting all by student id {} ", studentId);

        String query = "" +
                "SELECT * " +
                "FROM students_tasks " +
                "INNER JOIN tasks ON students_tasks.task_id = tasks.task_id " +
                "WHERE student_id = ? ";
        return jdbcTemplate.query(query, new TaskMapper(), studentId);
    }

    @Override
    public boolean assignToStudent(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("assigning, student id {}, task id {}", studentId, taskId);

        String query = "" +
                "INSERT INTO students_tasks(student_id, task_id) " +
                "VALUES (?,?)";
        try {
            return jdbcTemplate.update(query, studentId, taskId) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("failed assigning, student id {}, task id {}", studentId, taskId, ex);
            throw new DaoException("task id=" + taskId + " student id=" + studentId, ex);
        }
    }

    @Override
    public boolean unassignFromStudent(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("un assigning, student id {}, task id {}", studentId, taskId);

        String query = "" +
                "DELETE FROM students_tasks " +
                "WHERE student_id = ? AND task_id = ?";
        try {
            return jdbcTemplate.update(query, studentId, taskId) == 1;

        } catch (DataAccessException ex) {

            LOGGER.error("failed un assigning, student id {}, task id {}", studentId, taskId, ex);
            throw new DaoException("task id=" + taskId + " student id=" + studentId, ex);
        }
    }

    @Override
    public boolean isStudentTaskRelationExist(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("is relation exist between student id = {}, task id = {}", studentId, taskId);

        String query = "" +
                "SELECT count(*) FROM students_tasks " +
                "WHERE student_id = ? AND task_id = ?";

        return jdbcTemplate.queryForObject(query, Integer.class, studentId, taskId) == 1;
    }

    @Override
    public boolean update(Task task) {
        LOGGER.debug("updating {}", task);

        String query = "" +
                "UPDATE  tasks " +
                "SET " +
                "task_name = ? ," +
                "task_description = ? " +
                "WHERE task_id = ? ";
        try {
            return jdbcTemplate.update(query, task.getName(), task.getDescription(), task.getId()) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", task, ex);
            throw new DaoException(task.toString(), ex);
        }
    }

    @Override
    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        String query = "" +
                "DELETE FROM tasks " +
                "WHERE task_id  = ? ";
        try {
            return jdbcTemplate.update(query, id) == 1;
        } catch (DataAccessException ex) {

            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException(id.toString(), ex);
        }
    }
}
