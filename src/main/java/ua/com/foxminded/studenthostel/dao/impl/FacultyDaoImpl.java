package ua.com.foxminded.studenthostel.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Faculty;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.math.BigInteger;

import java.util.List;

@Transactional
@Repository
public class FacultyDaoImpl implements FacultyDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigInteger insert(Faculty faculty) {
        LOGGER.debug("inserting faculty {}", faculty);
        try {
            entityManager.persist(faculty);
            entityManager.flush();

            BigInteger id = faculty.getId();
            LOGGER.debug("inserting complete, id = {}", id);
            return id;

        } catch (PersistenceException ex) {
            LOGGER.error("insertion error {}", faculty, ex);
            throw new DaoException("Insertion error: " + faculty, ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Faculty getById(BigInteger facultyId) {
        LOGGER.debug("getting by id {}", facultyId);

        Faculty faculty = entityManager.find(Faculty.class, facultyId);

        if (faculty == null) {
            LOGGER.warn("Failed get by id {}", facultyId);
            throw new NotFoundException("Failed get by id: " + facultyId);
        }
        LOGGER.debug("getting complete {}", faculty);
        return faculty;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Faculty> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {}, limit {}  ", offset, limit);

        return entityManager
                .createNamedQuery("Faculty.getAll", Faculty.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Faculty update(Faculty faculty) {
        LOGGER.debug("updating {}", faculty);
        try {
            Faculty result = entityManager.merge(faculty);
            entityManager.flush();

            LOGGER.debug("updating complete, result: {}", result);
            return result;

        } catch (PersistenceException ex) {
            LOGGER.error("updating error {}", faculty, ex);
            throw new DaoException("Updating error: " + faculty, ex);
        }
    }

    @Override
    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);
        try {
            Faculty faculty = entityManager.find(Faculty.class, id);
            entityManager.remove(faculty);
            entityManager.flush();

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }
}
