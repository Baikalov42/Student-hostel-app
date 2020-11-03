package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.studenthostel.dao.GroupDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Group;

import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;

import java.util.List;

@Service
public class GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private CourseNumberService courseNumberService;
    @Autowired
    private ValidatorEntity<Group> validator;


    public BigInteger insert(Group group) {
        LOGGER.debug("inserting {}", group);

        validator.validate(group);

        facultyService.validateExistence(group.getFaculty().getId());
        courseNumberService.validateExistence(group.getCourseNumber().getId());

        BigInteger id = groupDao.insert(group);

        LOGGER.debug("inserting complete, id = {}", id);
        return id;
    }

    public Group getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        Group group = groupDao.getById(id);

        LOGGER.debug("getting complete {}", group);
        return group;
    }

    public List<Group> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {} , limit {} ", offset, limit);
        List<Group> result = groupDao.getAll(offset, limit);

        if (result.isEmpty()) {

            LOGGER.warn("result is empty, offset = {}, limit = {}", offset, limit);
            throw new NotFoundException("Result with offset=" + offset + " and limit=" + limit + " is empty");
        }
        return result;
    }

    public Group update(Group group) {
        LOGGER.debug("updating {}", group);

        validator.validate(group);
        validator.validateId(group.getId());

        validateExistence(group.getId());

        facultyService.validateExistence(group
                .getFaculty()
                .getId());

        courseNumberService.validateExistence(group
                .getCourseNumber()
                .getId());

        return groupDao.update(group);
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        groupDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);
        try {
            groupDao.getById(id);

        } catch (NotFoundException ex) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}

