package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.repository.TaskRepository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ValidatorEntity<Task> validator;

    public BigInteger insert(Task task) {
        LOGGER.debug("inserting {}", task);

        validator.validate(task);
        try {
            return taskRepository.save(task).getId();

        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", task, ex);
            throw new DaoException("Insertion error : " + task, ex);
        }
    }

    public Task getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);

        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + id));
    }

    public List<Task> getAll(int pageNumber) {
        LOGGER.debug("getting all, pageNumber = {}, pageSize = {} ", pageNumber, PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<Task> result = taskRepository.findAll(pageable).getContent();

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, pageNumber = {} ", pageNumber);
            throw new NotFoundException("Result with pageNumber =" + pageNumber + " is empty");
        }
        return result;
    }

    public List<Task> getAllByStudent(BigInteger studentId) {
        LOGGER.debug("getting all by student id {} ", studentId);

        validator.validateId(studentId);
        studentService.validateExistence(studentId);

        List<Task> tasks = taskRepository.getAllByStudent(studentId);

        if (tasks.isEmpty()) {
            LOGGER.warn("result is empty, student id = {}", studentId);
            throw new NotFoundException("result is empty, student id = " + studentId);
        }
        return tasks;
    }

    public void assignToStudent(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("assigning, student id {}, task id {}", studentId, taskId);

        validator.validateId(studentId, taskId);
        validateExistence(taskId);
        studentService.validateExistence(studentId);

        taskRepository.assignToStudent(studentId, taskId);
    }

    public void unassignFromStudent(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("un assigning, student id {}, task id {}", studentId, taskId);

        validator.validateId(studentId, taskId);
        validateExistence(taskId);
        studentService.validateExistence(studentId);

        taskRepository.unassignFromStudent(studentId, taskId);
    }

    public boolean isStudentTaskRelationExist(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("is relation exist between student id = {}, task id = {}", studentId, taskId);

        validator.validateId(studentId, taskId);
        validateExistence(taskId);
        studentService.validateExistence(studentId);

        return taskRepository.isStudentTaskRelationExist(studentId, taskId);
    }


    public Task update(Task task) {
        LOGGER.debug("updating {}", task);

        validator.validate(task);
        validator.validateId(task.getId());
        validateExistence(task.getId());

        try {
            return taskRepository.save(task);

        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", task, ex);
            throw new DaoException("Updating error: " + task, ex);
        }
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        try {
            taskRepository.deleteById(id);

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);

        if (!taskRepository.existsById(id)) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist");
        }
    }
}
