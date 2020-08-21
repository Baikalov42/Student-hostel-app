package ua.com.foxminded.studenthostel.models.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Task;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TaskMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet resultSet, int i) throws SQLException {
        ua.com.foxminded.studenthostel.models.Task task = new ua.com.foxminded.studenthostel.models.Task();
        task.setName(resultSet.getString("task_name"));
        task.setDescription(resultSet.getString("task_description"));
        task.setCostInHours(resultSet.getInt("cost"));
        task.setId(resultSet.getInt("task_id"));

        return task;
    }
}
