package ua.com.foxminded.studenthostel.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Floor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.math.BigInteger;
import java.util.List;

@Transactional
@Repository
public class FloorDaoImpl implements FloorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(FloorDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigInteger insert(Floor floor) {
        LOGGER.debug("inserting floor {}", floor);
        try {
            entityManager.persist(floor);
            entityManager.flush();

            BigInteger id = floor.getId();
            LOGGER.debug("inserting complete, id = {}", id);
            return id;

        } catch (PersistenceException ex) {
            LOGGER.error("insertion error {}", floor, ex);
            throw new DaoException("Insertion error: " + floor, ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Floor getById(BigInteger floorId) {
        LOGGER.debug("getting by id {}", floorId);

        Floor floor = entityManager.find(Floor.class, floorId);

        if (floor == null) {
            LOGGER.warn("Failed get by id {}", floorId);
            throw new NotFoundException("Failed get by id: " + floorId);
        }
        LOGGER.debug("getting complete {}", floor);
        return floor;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Floor> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {}, limit {}  ", offset, limit);

        return entityManager
                .createNamedQuery("Floor.getAll", Floor.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Floor update(Floor floor) {
        LOGGER.debug("updating {}", floor);

        try {
            Floor result = entityManager.merge(floor);
            entityManager.flush();

            LOGGER.debug("updating complete, result: {}", result);
            return result;

        } catch (PersistenceException ex) {
            LOGGER.error("updating error {}", floor, ex);
            throw new DaoException("Updating error: " + floor, ex);
        }
    }

    @Override
    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        try {
            Floor floor = entityManager.find(Floor.class, id);
            entityManager.remove(floor);
            entityManager.flush();

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }
}
