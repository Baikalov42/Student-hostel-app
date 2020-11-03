package ua.com.foxminded.studenthostel.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.dao.GroupDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Group;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

@Repository
@Transactional
public class GroupDaoImpl implements GroupDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BigInteger insert(Group group) {
        LOGGER.debug("inserting group {}", group);
        try {
            entityManager.persist(group);

            BigInteger id = group.getId();
            LOGGER.debug("inserting complete, id = {}", id);
            return id;

        } catch (DataIntegrityViolationException ex) {
            throw new DaoException("Insertion error: " + group, ex);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Group getById(BigInteger groupId) {
        LOGGER.debug("getting by id {}", groupId);

        Group group = entityManager.find(Group.class, groupId);

        if (group == null) {
            LOGGER.warn("Failed get by id {}", groupId);
            throw new NotFoundException("Failed get by id: " + groupId);
        }
        LOGGER.debug("getting complete {}", group);
        return group;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Group> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {}, limit {}  ", offset, limit);

        return entityManager
                .createNamedQuery("Group.getAll", Group.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Group update(Group group) {
        LOGGER.debug("updating {}", group);

        try {
            Group result = entityManager.merge(group);
            LOGGER.debug("updating complete, result: {}", result);
            return result;

        } catch (DataIntegrityViolationException ex) {
            LOGGER.error("updating error {}", group, ex);
            throw new DaoException("Updating error: " + group, ex);
        }
    }

    @Override
    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        try {
            Group group = entityManager.find(Group.class, id);
            entityManager.remove(group);

        } catch (DataIntegrityViolationException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }
}
