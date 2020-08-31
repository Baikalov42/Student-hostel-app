package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.models.mappers.TaskMapper;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;


@Component
public class TaskDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Qualifier("taskJdbcInsert")
    @Autowired
    private SimpleJdbcInsert taskJdbcInsert;

    public BigInteger insert(Task task) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("task_name", task.getName());
        parameters.put("task_description", task.getDescription());
        parameters.put("cost", task.getCostInHours());

        return BigInteger.valueOf(taskJdbcInsert.executeAndReturnKey(parameters).longValue());
    }

    public Task getById(BigInteger taskId) {
        String query = "" +
                "SELECT * " +
                "FROM tasks " +
                "WHERE task_id = ? ";
        return jdbcTemplate.queryForObject(query, new TaskMapper(), taskId);

    }

    public boolean setToStudent(BigInteger taskId, BigInteger studentId) {
        String query = "" +
                "INSERT INTO students_tasks(student_id, task_id) " +
                "VALUES (?,?)";

        return jdbcTemplate.update(query, studentId, taskId) == 1;
    }

    public boolean removeFromStudent(BigInteger studentId, BigInteger taskId) {
        String query = "" +
                "DELETE FROM students_tasks " +
                "WHERE student_id = ? AND task_id = ?";

        return jdbcTemplate.update(query, studentId, taskId) == 1;
    }

    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM tasks " +
                "WHERE task_id  = ? ";

        return jdbcTemplate.update(query, id) == 1;
    }
}
