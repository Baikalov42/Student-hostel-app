package ua.com.foxminded.studenthostel.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.models.mappers.EquipmentMapper;
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
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"task_id"});
            ps.setString(1, task.getName());
            ps.setString(2, task.getDescription());
            ps.setInt(3, task.getCostInHours());

            return ps;
        }, keyHolder);

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
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("failed to get object");
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
                "WHERE student_id = ? ";
        return jdbcTemplate.query(query, new TaskMapper(), studentId);
    }


    @Override
    public boolean assignToStudent(BigInteger studentId, BigInteger taskId) {
        String query = "" +
                "INSERT INTO students_tasks(student_id, task_id) " +
                "VALUES (?,?)";

        return jdbcTemplate.update(query, studentId, taskId) == 1;
    }

    @Override
    public boolean removeFromStudent(BigInteger studentId, BigInteger taskId) {
        String query = "" +
                "DELETE FROM students_tasks " +
                "WHERE student_id = ? AND task_id = ?";

        return jdbcTemplate.update(query, studentId, taskId) == 1;
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

        return jdbcTemplate.update(query, task.getName(), task.getDescription(), task.getId()) == 1;
    }

    @Override
    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM tasks " +
                "WHERE task_id  = ? ";

        return jdbcTemplate.update(query, id) == 1;
    }
}
