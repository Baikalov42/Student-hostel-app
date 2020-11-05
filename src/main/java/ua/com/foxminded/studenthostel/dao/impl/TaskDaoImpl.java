package ua.com.foxminded.studenthostel.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.dao.StudentDao;
import ua.com.foxminded.studenthostel.dao.TaskDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.Task;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.math.BigInteger;
import java.util.List;

@Transactional
@Repository
public class TaskDaoImpl implements TaskDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private StudentDao studentDao;

    @Override
    public BigInteger insert(Task task) {
        LOGGER.debug("inserting task {}", task);

        try {
            entityManager.persist(task);
            entityManager.flush();

            BigInteger id = task.getId();
            LOGGER.debug("inserting complete, id = {}", id);
            return id;

        } catch (PersistenceException ex) {
            LOGGER.error("insertion error {}", task, ex);
            throw new DaoException("Insertion error: " + task, ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Task getById(BigInteger taskId) {
        LOGGER.debug("getting by id {}", taskId);

        Task task = entityManager.find(Task.class, taskId);

        if (task == null) {
            LOGGER.warn("Failed get by id {}", taskId);
            throw new NotFoundException("Failed get by id: " + taskId);
        }
        LOGGER.debug("getting complete {}", task);
        return task;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {}, limit {}  ", offset, limit);

        return entityManager
                .createNamedQuery("Task.getAll", Task.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> getAllByStudent(BigInteger studentId) {
        LOGGER.debug("getting all by Student {}", studentId);

        return entityManager
                .createQuery("" +
                        "select ta " +
                        "from Task ta " +
                        "inner join ta.students st " +
                        "where st.id = :id ", Task.class)
                .setParameter("id", studentId)
                .getResultList();
    }

    @Override
    public void assignToStudent(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("assigning, student {}, task {}", studentId, taskId);
        try {
            Student student = studentDao.getById(studentId);
            Task task = this.getById(taskId);
            task.addStudent(student);
            entityManager.flush();

        } catch (PersistenceException ex) {

            LOGGER.error("failed assigning, student id {}, equipment id {}", studentId, taskId, ex);
            String message = "student id =" + studentId + " equipment id =" + taskId;
            throw new DaoException(message, ex);
        }
    }

    @Override
    public void unassignFromStudent(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("un assigning, student {}, task {}", studentId, taskId);
        try {
            Student student = studentDao.getById(studentId);
            Task task = this.getById(taskId);
            task.removeStudent(student);
            entityManager.flush();

        } catch (DataAccessException ex) {
            LOGGER.error("failed un assigning, student id {}, equipment id {}", studentId, taskId, ex);
            throw new DaoException("student id =" + studentId + " equipment id =" + taskId, ex);
        }
    }

    @Override
    public boolean isStudentTaskRelationExist(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("is student {} , contains task {}", studentId, taskId);

        Task task = entityManager.find(Task.class, taskId);
        Student student = entityManager.find(Student.class, studentId);

        if (task == null || student == null) {
            LOGGER.warn("not exist, student {}, task {}", studentId, taskId);
            throw new NotFoundException("not exist student: " + studentId + " task: " + taskId);
        }
        return task.getStudents().contains(student);
    }

    @Override
    public Task update(Task task) {
        LOGGER.debug("updating {}", task);

        try {
            Task result = entityManager.merge(task);
            entityManager.flush();

            LOGGER.debug("updating complete, result: {}", result);
            return result;

        } catch (PersistenceException ex) {
            LOGGER.error("updating error {}", task, ex);
            throw new DaoException("Updating error: " + task, ex);
        }
    }

    @Override
    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        try {
            Task task = entityManager.find(Task.class, id);
            entityManager.remove(task);
            entityManager.flush();

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }
}
