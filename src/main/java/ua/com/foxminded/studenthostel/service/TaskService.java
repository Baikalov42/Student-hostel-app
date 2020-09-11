package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.TaskDao;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.service.utils.ValidationsUtils;

import java.math.BigInteger;
import java.util.List;

@Service
public class TaskService {

    private static final String NAME_PATTERN = "[\\w\\s]{3,29}";
    @Autowired
    TaskDao taskDao;

    public BigInteger insert(Task task) throws ValidationException {
        ValidationsUtils.validateName(task.getName(), NAME_PATTERN);
        return taskDao.insert(task);
    }

    public Task getById(BigInteger id) throws ValidationException {
        ValidationsUtils.validateId(id);
        return taskDao.getById(id);
    }

    public List<Task> getAll(long limit, long offset) {
        return taskDao.getAll(limit, offset);
    }

    public boolean assignToStudent(BigInteger studentId, BigInteger taskId) {
        return taskDao.assignToStudent(studentId, taskId);
    }

    public boolean removeFromStudent(BigInteger studentId, BigInteger taskId) {
        return taskDao.removeFromStudent(studentId, taskId);
    }

    public boolean update(Task task) throws ValidationException {
        ValidationsUtils.validateName(task.getName(), NAME_PATTERN);
        return taskDao.update(task);
    }

    public boolean deleteById(BigInteger id) {
        return taskDao.deleteById(id);
    }
}
