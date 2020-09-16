package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.TaskDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ValidatorEntity<Task> validator;

    public BigInteger insert(Task task) throws ValidationException {

        validator.validateEntity(task);
        return taskDao.insert(task);
    }

    public Task getById(BigInteger id) throws ValidationException {
        validator.validateId(id);
        return taskDao.getById(id);
    }

    public List<Task> getAll(long limit, long offset) throws ValidationException {
        long countOfEntries = taskDao.getEntriesCount().longValue();

        if (countOfEntries <= offset) {
            throw new ValidationException("offset is greater than the number of entries");
        }
        return taskDao.getAll(limit, offset);
    }

    public boolean assignToStudent(BigInteger studentId, BigInteger taskId) throws ValidationException {

        validator.validateId(studentId, taskId);
        return taskDao.assignToStudent(studentId, taskId);
    }

    public boolean removeFromStudent(BigInteger studentId, BigInteger taskId) throws ValidationException {

        validator.validateId(studentId, taskId);
        return taskDao.removeFromStudent(studentId, taskId);
    }

    public boolean update(Task task) throws ValidationException {

        validator.validateEntity(task);
        validator.validateId(task.getId());
        validateForExist(task.getId());

        return taskDao.update(task);
    }

    public boolean deleteById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        validateForExist(id);

        return taskDao.deleteById(id);
    }

    private void validateForExist(BigInteger id) throws ValidationException {
        try {
            taskDao.getById(id);
        } catch (DaoException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
