package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.TaskDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskDao taskDao;
    @Autowired
    StudentService studentService;
    @Autowired
    private ValidatorEntity<Task> validator;

    public BigInteger insert(Task task) {

        validator.validate(task);
        return taskDao.insert(task);
    }

    public Task getById(BigInteger id) {
        validator.validateId(id);
        return taskDao.getById(id);
    }

    public List<Task> getAll(long limit, long offset) {
        List<Task> result = taskDao.getAll(limit, offset);

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, limit = {}, offset = {}", limit, offset);
            throw new NotFoundException("Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean assignToStudent(BigInteger studentId, BigInteger taskId) {

        validator.validateId(studentId, taskId);
        validateExistence(taskId);
        studentService.validateExistence(studentId);

        return taskDao.assignToStudent(studentId, taskId);
    }

    public boolean unassignFromStudent(BigInteger studentId, BigInteger taskId) {

        validator.validateId(studentId, taskId);
        validateExistence(taskId);
        studentService.validateExistence(studentId);

        return taskDao.unassignFromStudent(studentId, taskId);
    }

    public boolean isStudentTaskRelationExist(BigInteger studentId, BigInteger taskId) {

        validator.validateId(studentId, taskId);
        validateExistence(taskId);
        studentService.validateExistence(studentId);

        return taskDao.isStudentTaskRelationExist(studentId, taskId);
    }


    public boolean update(Task task) {

        validator.validate(task);
        validator.validateId(task.getId());
        validateExistence(task.getId());

        return taskDao.update(task);
    }

    public boolean deleteById(BigInteger id) {

        validator.validateId(id);
        validateExistence(id);

        return taskDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);

        try {
            taskDao.getById(id);
        } catch (NotFoundException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
