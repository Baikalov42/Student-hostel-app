package ua.com.foxminded.studenthostel.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.CourseNumber;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

@Transactional
@Repository
public class CourseNumberDaoImpl implements CourseNumberDao {


    private static final Logger LOGGER = LoggerFactory.getLogger(CourseNumberDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigInteger insert(CourseNumber courseNumber) {
        LOGGER.debug("inserting {}", courseNumber);

        try {
            entityManager.persist(courseNumber);

            BigInteger id = courseNumber.getId();
            LOGGER.debug("inserting complete, id = {}", id);
            return id;

        } catch (DataIntegrityViolationException ex) {
            throw new DaoException("Insertion error: " + courseNumber, ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public CourseNumber getById(BigInteger courseNumberId) {
        LOGGER.debug("getting by id {}", courseNumberId);

        CourseNumber courseNumber = entityManager.find(CourseNumber.class, courseNumberId);

        if (courseNumber == null) {
            LOGGER.warn("Failed get by id {}", courseNumberId);
            throw new NotFoundException("Failed get by id: " + courseNumberId);
        }
        LOGGER.debug("getting complete {}", courseNumber);
        return courseNumber;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseNumber> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {}, limit {}  ", offset, limit);

        return entityManager
                .createNamedQuery("CourseNumber.getAll", CourseNumber.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public CourseNumber update(CourseNumber courseNumber) {
        LOGGER.debug("updating {}", courseNumber);

        try {
            CourseNumber result = entityManager.merge(courseNumber);
            LOGGER.debug("updating complete, result: {}", result);
            return result;

        } catch (DataIntegrityViolationException ex) {
            LOGGER.error("updating error {}", courseNumber, ex);
            throw new DaoException("Updating error: " + courseNumber, ex);
        }
    }

    @Override
    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        try {
            CourseNumber courseNumber = entityManager.find(CourseNumber.class, id);
            entityManager.remove(courseNumber);

        } catch (DataIntegrityViolationException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }
}
