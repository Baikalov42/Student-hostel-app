package ua.com.foxminded.studenthostel.dao.impl;


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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Task task) {
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

        } catch (DataAccessException ex) {
            throw new DaoException(task.toString(), ex);
        }
        return BigInteger.valueOf(keyHolder.getKey().longValue());
    }

    @Override
    public Task getById(BigInteger taskId) {
        String query = "" +
                "SELECT * " +
                "FROM tasks " +
                "WHERE task_id = ? ";
        try {
            return jdbcTemplate.queryForObject(query, new TaskMapper(), taskId);

        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(taskId.toString(), ex);
        }
    }

    @Override
    public List<Task> getAll(long limit, long offset) {
        String query = "" +
                "SELECT * " +
                "FROM tasks " +
                "ORDER BY task_id " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, new TaskMapper(), limit, offset);
    }

    @Override
    public List<Task> getAllByStudent(BigInteger studentId) {
        String query = "" +
                "SELECT * " +
                "FROM students_tasks " +
                "INNER JOIN tasks ON students_tasks.task_id = tasks.task_id " +
                "WHERE student_id = ? ";
        return jdbcTemplate.query(query, new TaskMapper(), studentId);
    }


    @Override
    public boolean assignToStudent(BigInteger studentId, BigInteger taskId) {
        String query = "" +
                "INSERT INTO students_tasks(student_id, task_id) " +
                "VALUES (?,?)";
        try {
            return jdbcTemplate.update(query, studentId, taskId) == 1;

        } catch (DataAccessException ex) {
            throw new DaoException("task id=" + taskId + " student id=" + studentId, ex);
        }
    }

    @Override
    public boolean unassignFromStudent(BigInteger studentId, BigInteger taskId) {
        String query = "" +
                "DELETE FROM students_tasks " +
                "WHERE student_id = ? AND task_id = ?";
        try {
            return jdbcTemplate.update(query, studentId, taskId) == 1;

        } catch (DataAccessException ex) {
            throw new DaoException("task id=" + taskId + " student id=" + studentId, ex);
        }
    }

    @Override
    public boolean isStudentTaskRelationExist(BigInteger studentId, BigInteger taskId) {
        String query = "" +
                "SELECT count(*) FROM students_tasks " +
                "WHERE student_id = ? AND task_id = ?";

        return jdbcTemplate.queryForObject(query, Integer.class, studentId, taskId) == 1;
    }

    @Override
    public BigInteger getEntriesCount() {
        String query = "" +
                "SELECT count(*) " +
                "FROM tasks";

        return jdbcTemplate.queryForObject(query, BigInteger.class);
    }

    @Override
    public boolean update(Task task) {
        String query = "" +
                "UPDATE  tasks " +
                "SET " +
                "task_name = ? ," +
                "task_description = ? " +
                "WHERE task_id = ? ";
        try {
            return jdbcTemplate.update(query, task.getName(), task.getDescription(), task.getId()) == 1;

        } catch (DataAccessException ex) {
            throw new DaoException(task.toString(), ex);
        }
    }

    @Override
    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM tasks " +
                "WHERE task_id  = ? ";
        try {
            return jdbcTemplate.update(query, id) == 1;
        } catch (DataAccessException ex) {
            throw new DaoException(id.toString(), ex);
        }
    }
}
