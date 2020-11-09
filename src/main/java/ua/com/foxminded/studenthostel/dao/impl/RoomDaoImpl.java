package ua.com.foxminded.studenthostel.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.dao.RoomDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Room;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.math.BigInteger;
import java.util.List;


@Repository
@Transactional
public class RoomDaoImpl implements RoomDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigInteger insert(Room room) {
        LOGGER.debug("inserting room {}", room);
        try {
            entityManager.persist(room);
            entityManager.flush();

            BigInteger id = room.getId();
            LOGGER.debug("inserting complete, id = {}", id);
            return id;

        } catch (PersistenceException ex) {
            LOGGER.error("insertion error {}", room, ex);
            throw new DaoException("Insertion error: " + room, ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Room getById(BigInteger roomId) {
        LOGGER.debug("getting by id {}", roomId);

        Room room = entityManager.find(Room.class, roomId);

        if (room == null) {
            LOGGER.warn("Failed get by id {}", roomId);
            throw new NotFoundException("Failed get by id: " + roomId);
        }
        LOGGER.debug("getting complete {}", room);
        return room;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Room> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {}, limit {}  ", offset, limit);

        return entityManager
                .createNamedQuery("Room.getAll", Room.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Room> getAllByEquipment(BigInteger equipmentId) {
        LOGGER.debug("getting all by Equipment id {}", equipmentId);

        return entityManager
                .createQuery("" +
                        "select rm " +
                        "from Room rm " +
                        "join rm.students st " +
                        "join  st.equipments eq " +
                        "where eq.id = :id ", Room.class)
                .setParameter("id", equipmentId)
                .getResultList();
    }

    @Override
    public Room update(Room room) {
        LOGGER.debug("updating {}", room);

        try {
            Room result = entityManager.merge(room);
            entityManager.flush();

            LOGGER.debug("updating complete, result: {}", result);
            return result;

        } catch (PersistenceException ex) {
            LOGGER.error("updating error {}", room, ex);
            throw new DaoException("Updating error: " + room, ex);
        }
    }

    @Override
    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        try {
            Room room = entityManager.find(Room.class, id);
            entityManager.remove(room);
            entityManager.flush();

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }
}
