package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.dao.GroupDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.dto.GroupDTO;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private FacultyDao facultyDao;
    @Autowired
    private CourseNumberDao courseNumberDao;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private CourseNumberService courseNumberService;
    @Autowired
    private ValidatorEntity<Group> validator;


    public BigInteger insert(Group group) {
        LOGGER.debug("inserting {}", group);

        validator.validate(group);

        facultyService.validateExistence(group.getFacultyId());
        courseNumberService.validateExistence(group.getCourseNumberId());

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

    public GroupDTO getDTOById(BigInteger id) {

        Group group = getById(id);
        return getDTO(group);
    }

    public List<Group> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);
        List<Group> result = groupDao.getAll(limit, offset);

        if (result.isEmpty()) {

            LOGGER.warn("result is empty, limit = {}, offset = {}", limit, offset);
            throw new NotFoundException("Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public List<GroupDTO> getAllDTO(long limit, long offset) {
        LOGGER.debug("getting all DTO, limit {} , offset {} ", limit, offset);

        List<Group> groups = getAll(limit, offset);
        List<GroupDTO> groupDTOS = new ArrayList<>(groups.size());

        for (Group group : groups) {
            groupDTOS.add(getDTO(group));
        }
        return groupDTOS;
    }

    public boolean update(Group group) {
        LOGGER.debug("updating {}", group);

        validator.validate(group);
        validator.validateId(group.getId());

        validateExistence(group.getId());
        facultyService.validateExistence(group.getFacultyId());
        courseNumberService.validateExistence(group.getCourseNumberId());

        return groupDao.update(group);
    }

    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        return groupDao.deleteById(id);
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

    GroupDTO getDTO(Group group) {
        LOGGER.debug("getting DTO,  {}", group);

        GroupDTO groupDTO = new GroupDTO();

        groupDTO.setId(group.getId());
        groupDTO.setName(group.getName());
        groupDTO.setCourseNumber(courseNumberDao.getById(group.getId()));
        groupDTO.setFaculty(facultyDao.getById(group.getId()));

        LOGGER.debug("getting DTO complete,  {}", groupDTO);

        return groupDTO;
    }
}

