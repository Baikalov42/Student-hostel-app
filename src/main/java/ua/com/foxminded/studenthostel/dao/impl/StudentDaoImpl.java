package ua.com.foxminded.studenthostel.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.dao.RoomDao;
import ua.com.foxminded.studenthostel.dao.StudentDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.Student;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;

@Repository
@Transactional
public class StudentDaoImpl implements StudentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RoomDao roomDao;

    @Override
    public BigInteger insert(Student student) {
        LOGGER.debug("inserting student {}", student);
        try {
            entityManager.persist(student);
            entityManager.flush();

            BigInteger id = student.getId();
            LOGGER.debug("inserting complete, id = {}", id);
            return id;

        } catch (PersistenceException ex) {
            LOGGER.error("insertion error {}", student, ex);
            throw new DaoException("Insertion error: " + student, ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Student getById(BigInteger studentId) {
        LOGGER.debug("getting by id {}", studentId);

        Student student = entityManager.find(Student.class, studentId);

        if (student == null) {
            LOGGER.warn("Failed get by id {}", studentId);
            throw new NotFoundException("Failed get by id: " + studentId);
        }
        LOGGER.debug("getting complete {}", student);
        return student;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Student> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {}, limit {}  ", offset, limit);

        return entityManager
                .createNamedQuery("Student.getAll", Student.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Student> getAllByFloor(BigInteger floorId) {
        LOGGER.debug("getting all by Floor id {}", floorId);

        return entityManager
                .createQuery("" +
                        "select st " +
                        "from Student st " +
                        "join st.room ro " +
                        "join ro.floor fl " +
                        "where fl.id =:id", Student.class)
                .setParameter("id", floorId)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Student> getAllByFaculty(BigInteger facultyId) {
        LOGGER.debug("getting all by Faculty id {}", facultyId);

        return entityManager
                .createQuery("" +
                        "select st " +
                        "from Student st " +
                        "join st.group gr " +
                        "join gr.faculty fa " +
                        "where fa.id = :id", Student.class)
                .setParameter("id", facultyId)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Student> getAllByCourse(BigInteger courseId) {
        LOGGER.debug("getting all by Course id {}", courseId);

        return entityManager
                .createQuery("" +
                        "select st " +
                        "from Student st " +
                        "join st.group gr " +
                        "join gr.courseNumber cn " +
                        "where cn.id = :id ", Student.class)
                .setParameter("id", courseId)
                .getResultList();

    }

    @Transactional(readOnly = true)
    @Override
    public List<Student> getAllWithDebitByGroup(BigInteger groupId, int numberOfHoursDebt) {
        LOGGER.debug("getting all by Group {}, debt {}", groupId, numberOfHoursDebt);

        return entityManager
                .createQuery("" +
                        "select st " +
                        "from Student st " +
                        "join st.group gr " +
                        "where gr.id = :id " +
                        "and st.hoursDebt > :debt ", Student.class)
                .setParameter("id", groupId)
                .setParameter("debt", numberOfHoursDebt)
                .getResultList();
    }

    @Override
    public Student changeRoom(BigInteger newRoomId, BigInteger studentId) {
        LOGGER.debug("changeRoom, new room {}, student {}", newRoomId, studentId);

        Student student = this.getById(studentId);
        Room newRoom = roomDao.getById(newRoomId);

        student.setRoom(newRoom);

        try {
            Student result = entityManager.merge(student);
            entityManager.flush();

            LOGGER.debug("changeRoom complete, result: {}", result);
            return result;

        } catch (PersistenceException ex) {
            LOGGER.error("changeRoom error student {}, room {}", studentId, newRoomId);
            throw new DaoException("changeRoom error student: + " + student + " room: " + newRoom);
        }
    }

    @Override
    public Student changeDebt(int newHoursDebt, BigInteger studentId) {
        LOGGER.debug("changeDebt, new debt {}, student {}", newHoursDebt, studentId);

        Student student = this.getById(studentId);
        student.setHoursDebt(newHoursDebt);

        try {
            Student result = entityManager.merge(student);
            entityManager.flush();

            LOGGER.debug("changeDebt complete, result: {}", result);
            return result;

        } catch (PersistenceException ex) {
            LOGGER.error("changeDebt error student {}, debt {}", studentId, newHoursDebt);
            throw new DaoException("changeDebt error student: + " + student + " debt: " + newHoursDebt);
        }
    }

    @Override
    public Student update(Student student) {
        LOGGER.debug("updating {}", student);

        try {
            Student result = entityManager.merge(student);
            entityManager.flush();

            LOGGER.debug("updating complete, result: {}", result);
            return result;

        } catch (PersistenceException ex) {
            LOGGER.error("updating error {}", student, ex);
            throw new DaoException("Updating error: " + student, ex);
        }
    }

    @Override
    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        try {
            Student student = entityManager.find(Student.class, id);
            entityManager.remove(student);
            entityManager.flush();

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }
}
