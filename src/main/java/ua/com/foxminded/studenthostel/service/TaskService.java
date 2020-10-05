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
        LOGGER.debug("inserting {}", task);

        validator.validate(task);
        BigInteger id = taskDao.insert(task);

        LOGGER.debug("inserting complete, id = {}", id);
        return id;
    }

    public Task getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        Task task = taskDao.getById(id);

        LOGGER.debug("getting complete {}", task);
        return task;
    }

    public List<Task> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);
        List<Task> result = taskDao.getAll(limit, offset);

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, limit = {}, offset = {}", limit, offset);
            throw new NotFoundException("Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean assignToStudent(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("assigning, student id {}, task id {}", studentId, taskId);

        validator.validateId(studentId, taskId);
        validateExistence(taskId);
        studentService.validateExistence(studentId);

        return taskDao.assignToStudent(studentId, taskId);
    }

    public boolean unassignFromStudent(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("un assigning, student id {}, task id {}", studentId, taskId);

        validator.validateId(studentId, taskId);
        validateExistence(taskId);
        studentService.validateExistence(studentId);

        return taskDao.unassignFromStudent(studentId, taskId);
    }

    public boolean isStudentTaskRelationExist(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("is relation exist between student id = {}, task id = {}", studentId, taskId);

        validator.validateId(studentId, taskId);
        validateExistence(taskId);
        studentService.validateExistence(studentId);

        return taskDao.isStudentTaskRelationExist(studentId, taskId);
    }


    public boolean update(Task task) {
        LOGGER.debug("updating {}", task);

        validator.validate(task);
        validator.validateId(task.getId());
        validateExistence(task.getId());

        return taskDao.update(task);
    }

    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        return taskDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);
        try {
            taskDao.getById(id);

        } catch (NotFoundException ex) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
