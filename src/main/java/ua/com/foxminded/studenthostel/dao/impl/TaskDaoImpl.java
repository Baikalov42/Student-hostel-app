package ua.com.foxminded.studenthostel.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

            BigInteger id = task.getId();
            LOGGER.debug("inserting complete, id = {}", id);
            return id;

        } catch (DataIntegrityViolationException ex) {
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

        Student student = studentDao.getById(studentId);
        Task task = this.getById(taskId);

        int before = student.getTasks().size();
        task.addStudent(student);
        int after = student.getTasks().size();

        if (before == after) {
            LOGGER.warn("Duplicate task: {}", taskId);
            throw new DaoException("Duplicate task: " + taskId);
        }
    }

    @Override
    public void unassignFromStudent(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("un assigning, student {}, task {}", studentId, taskId);

        Student student = studentDao.getById(studentId);
        Task task = this.getById(taskId);

        int before = student.getTasks().size();
        task.removeStudent(student);
        int after = student.getTasks().size();

        if (before == after) {
            LOGGER.warn("Student dont have task: {}", taskId);
            throw new DaoException("Student dont have task:: " + taskId);
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
            LOGGER.debug("updating complete, result: {}", result);
            return result;

        } catch (DataIntegrityViolationException ex) {
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

        } catch (DataIntegrityViolationException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }
}
