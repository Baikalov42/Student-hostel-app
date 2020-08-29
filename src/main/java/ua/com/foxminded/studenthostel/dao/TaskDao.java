package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.models.mappers.TaskMapper;

import java.math.BigInteger;


@Component
public class TaskDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(Task task) {
        String query = "" +
                "INSERT INTO tasks (task_name, task_description, cost) " +
                "VALUES (?,?,?)";

        jdbcTemplate.update(query, task.getName(), task.getDescription(), task.getCostInHours());
    }

    public Task getById(BigInteger taskId) {
        String query = "" +
                "SELECT * " +
                "FROM tasks " +
                "WHERE task_id = ? ";
        return jdbcTemplate.queryForObject(query, new TaskMapper(), taskId);

    }

    public void setToStudent(BigInteger taskId, BigInteger studentId) {
        String query = "" +
                "INSERT INTO students_tasks(student_id, task_id) " +
                "VALUES (?,?)";

        jdbcTemplate.update(query, studentId, taskId);
    }

    public void removeFromStudent(BigInteger studentId, BigInteger taskId) {
        String query = "" +
                "DELETE FROM students_tasks " +
                "WHERE student_id = ? AND task_id = ?";

        jdbcTemplate.update(query, studentId, taskId);
    }

    public void deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM tasks " +
                "WHERE task_id  = ? ";

        jdbcTemplate.update(query, id);
    }
}
