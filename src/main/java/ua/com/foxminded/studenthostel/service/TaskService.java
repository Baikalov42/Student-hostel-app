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
    StudentService studentService;
    @Autowired
    private ValidatorEntity<Task> validator;

    public BigInteger insert(Task task) throws ValidationException {

        validator.validate(task);
        return taskDao.insert(task);
    }

    public Task getById(BigInteger id) throws ValidationException {
        validator.validateId(id);
        return taskDao.getById(id);
    }

    public List<Task> getAll(long limit, long offset) throws ValidationException {
        List<Task> result = taskDao.getAll(limit, offset);
        if (result.isEmpty()) {
            throw new ValidationException(
                    "Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean assignToStudent(BigInteger studentId, BigInteger taskId) throws ValidationException {

        validator.validateId(studentId, taskId);
        validateExistence(taskId);
        studentService.validateExistence(studentId);

        return taskDao.assignToStudent(studentId, taskId);
    }

    public boolean unassignFromStudent(BigInteger studentId, BigInteger taskId) throws ValidationException {

        validator.validateId(studentId, taskId);
        validateExistence(taskId);
        studentService.validateExistence(studentId);

        return taskDao.unassignFromStudent(studentId, taskId);
    }

    public boolean isStudentTaskRelationExist(BigInteger studentId, BigInteger taskId) throws ValidationException {

        validator.validateId(studentId, taskId);
        return taskDao.isStudentTaskRelationExist(studentId, taskId);
    }


    public boolean update(Task task) throws ValidationException {

        validator.validate(task);
        validator.validateId(task.getId());
        validateExistence(task.getId());

        return taskDao.update(task);
    }

    public boolean deleteById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        validateExistence(id);

        return taskDao.deleteById(id);
    }

    protected void validateExistence(BigInteger id) throws ValidationException {
        try {
            taskDao.getById(id);
        } catch (DaoException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
