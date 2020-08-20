package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.com.foxminded.studenthostel.models.Task;


import javax.sql.DataSource;


public class TaskDao {
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<Task> taskRowMapper = (resultSet, rowNum) -> {

        Task task = new Task();
        task.setName(resultSet.getString("task_name"));
        task.setDescription(resultSet.getString("task_description"));
        task.setCostInHours(resultSet.getInt("cost"));
        task.setId(resultSet.getInt("task_id"));

        return task;
    };

    @Autowired
    public TaskDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void insert(Task task) {
        String query = "" +
                "INSERT INTO tasks (task_name, task_description, cost) " +
                "VALUES (?,?,?)";

        jdbcTemplate.update(query, task.getName(), task.getDescription(), task.getCostInHours());
    }

    public Task getById(int taskId) {
        String query = "" +
                "SELECT * FROM tasks " +
                "WHERE task_id = ? ";
        return jdbcTemplate.queryForObject(query, taskRowMapper, taskId);

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
