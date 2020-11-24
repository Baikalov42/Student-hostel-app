package ua.com.foxminded.studenthostel.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.repository.StudentRepository;
import ua.com.foxminded.studenthostel.repository.TaskExtendedRepository;
import ua.com.foxminded.studenthostel.repository.TaskRepository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.Task;

import javax.persistence.PersistenceException;
import java.math.BigInteger;

@Repository
@Transactional
public class TaskExtendedRepositoryImpl implements TaskExtendedRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskExtendedRepositoryImpl.class);

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void assignToStudent(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("assigning, student {}, task {}", studentId, taskId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + studentId));

        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + taskId));

        task.addStudent(student);
        try {
            taskRepository.flush();

        } catch (PersistenceException ex) {

            LOGGER.error("failed assigning, student id {}, task id {}", studentId, taskId, ex);
            String message = "student id =" + studentId + " task id =" + taskId;
            throw new DaoException(message, ex);
        }
    }

    @Override
    public void unassignFromStudent(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("un assigning, student {}, task {}", studentId, taskId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + studentId));

        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + studentId));

        task.removeStudent(student);
        try {
            taskRepository.flush();

        } catch (DataAccessException ex) {
            LOGGER.error("failed un assigning, student id {}, task id {}", studentId, taskId, ex);
            throw new DaoException("student id =" + studentId + " task id =" + taskId, ex);
        }
    }

    @Override
    public boolean isStudentTaskRelationExist(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("is student {} , contains task {}", studentId, taskId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + studentId));

        Task task = this.taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + studentId));

        if (task == null || student == null) {
            LOGGER.warn("not exist, student {}, task {}", studentId, taskId);
            throw new NotFoundException("not exist student: " + studentId + " task: " + taskId);
        }
        return task.getStudents().contains(student);
    }
}
