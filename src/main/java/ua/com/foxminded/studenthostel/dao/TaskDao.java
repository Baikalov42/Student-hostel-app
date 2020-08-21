package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.models.mappers.TaskMapper;


@Component
public class TaskDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TaskMapper taskMapper;

    public void insert(Task task) {
        String query = "" +
                "INSERT INTO tasks (task_name, task_description, cost) " +
                "VALUES (?,?,?)";

        jdbcTemplate.update(query, task.getName(), task.getDescription(), task.getCostInHours());
    }

    public Task getById(int taskId) {
        String query = "" +
                "SELECT * " +
                "FROM tasks " +
                "WHERE task_id = ? ";
        return jdbcTemplate.queryForObject(query, taskMapper, taskId);

    }

    public void setToStudent(int taskID, int studentID) {
        String query = "" +
                "INSERT INTO students_tasks(student_id, task_id) " +
                "VALUES (?,?)";

        jdbcTemplate.update(query, studentID, taskID);
    }

    public void removeFromStudent(int studentID, int taskID) {
        String query = "" +
                "DELETE FROM students_tasks " +
                "WHERE student_id = ? AND task_id = ?";

        jdbcTemplate.update(query, studentID, taskID);
    }

}
