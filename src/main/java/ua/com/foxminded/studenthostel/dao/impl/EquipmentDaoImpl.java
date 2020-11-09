package ua.com.foxminded.studenthostel.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.dao.EquipmentDao;
import ua.com.foxminded.studenthostel.dao.StudentDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.Student;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.math.BigInteger;
import java.util.List;

@Transactional
@Repository
public class EquipmentDaoImpl implements EquipmentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private StudentDao studentDao;

    @Override
    public BigInteger insert(Equipment equipment) {
        LOGGER.debug("inserting equipment {}", equipment);

        try {
            entityManager.persist(equipment);
            entityManager.flush();

            BigInteger id = equipment.getId();
            LOGGER.debug("inserting complete, id = {}", id);
            return id;

        } catch (PersistenceException  ex) {
            LOGGER.error("insertion error {}", equipment, ex);
            throw new DaoException("Insertion error: " + equipment, ex);
        }
    }

    @Override
    public void assignToStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("assigning, student {}, equipment {}", studentId, equipmentId);


        try {
            Equipment equipment = this.getById(equipmentId);
            Student student = studentDao.getById(studentId);
            equipment.addStudent(student);
            entityManager.flush();

        } catch (PersistenceException  ex) {

            LOGGER.error("failed assigning, student id {}, equipment id {}", studentId, equipmentId, ex);
            String message = "student id =" + studentId + " equipment id =" + equipmentId;
            throw new DaoException(message, ex);
        }
    }

    @Override
    public void unassignFromStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("un assigning, student id {}, equipment id {}", studentId, equipmentId);



        try {
            Equipment equipment = this.getById(equipmentId);
            Student student = studentDao.getById(studentId);
            equipment.removeStudent(student);
            entityManager.flush();

        } catch (DataAccessException ex) {
            LOGGER.error("failed un assigning, student id {}, equipment id {}", studentId, equipmentId, ex);
            throw new DaoException("student id =" + studentId + " equipment id =" + equipmentId, ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Equipment getById(BigInteger equipmentId) {
        LOGGER.debug("getting by id {}", equipmentId);

        Equipment equipment = entityManager.find(Equipment.class, equipmentId);

        if (equipment == null) {
            LOGGER.warn("Failed get by id {}", equipmentId);
            throw new NotFoundException("Failed get by id: " + equipmentId);
        }
        LOGGER.debug("getting complete {}", equipment);
        return equipment;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Equipment> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {}, limit {}  ", offset, limit);

        return entityManager
                .createNamedQuery("Equipment.getAll", Equipment.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Equipment> getAllByStudent(BigInteger studentId) {
        LOGGER.debug("getting all by student, id {}", studentId);

        return entityManager
                .createQuery("" +
                        "select eq " +
                        "from Equipment eq " +
                        "join eq.students st " +
                        "where st.id =:id", Equipment.class)
                .setParameter("id", studentId)
                .getResultList();
    }

    @Override
    public Equipment update(Equipment equipment) {
        LOGGER.debug("updating {}", equipment);

        try {
            Equipment result = entityManager.merge(equipment);
            entityManager.flush();

            LOGGER.debug("updating complete, result: {}", result);
            return result;

        } catch (PersistenceException ex) {
            LOGGER.error("updating error {}", equipment, ex);
            throw new DaoException("Updating error: " + equipment, ex);
        }
    }

    @Override
    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        try {
            Equipment equipment = entityManager.find(Equipment.class, id);
            entityManager.remove(equipment);
            entityManager.flush();

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }
}
