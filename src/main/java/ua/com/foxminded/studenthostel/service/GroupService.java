package ua.com.foxminded.studenthostel.service;

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

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private FacultyDao facultyDao;
    @Autowired
    private CourseNumberDao courseNumberDao;
    @Autowired
    private ValidatorEntity<Group> validator;


    public BigInteger insert(Group group) {

        validator.validate(group);
        return groupDao.insert(group);
    }

    public Group getById(BigInteger id) {

        validator.validateId(id);
        return groupDao.getById(id);
    }

    public GroupDTO getDTOById(BigInteger id) {

        Group group = getById(id);
        return getDTO(group);
    }

    public List<Group> getAll(long limit, long offset) {
        List<Group> result = groupDao.getAll(limit, offset);

        if (result.isEmpty()) {
            throw new NotFoundException(
                    "Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public List<GroupDTO> getAllDTO(long limit, long offset) {
        List<Group> groups = getAll(limit, offset);
        List<GroupDTO> groupDTOS = new ArrayList<>(groups.size());

        for (Group group : groups) {
            groupDTOS.add(getDTO(group));
        }
        return groupDTOS;
    }

    public boolean update(Group group) {

        validator.validate(group);
        validator.validateId(group.getId());
        validateExistence(group.getId());

        return groupDao.update(group);
    }

    public boolean deleteById(BigInteger id) {

        validator.validateId(id);
        validateExistence(id);

        return groupDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        try {
            groupDao.getById(id);
        } catch (NotFoundException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }

    GroupDTO getDTO(Group group) {
        GroupDTO groupDTO = new GroupDTO();

        groupDTO.setId(group.getId());
        groupDTO.setName(group.getName());
        groupDTO.setCourseNumber(courseNumberDao.getById(group.getId()));
        groupDTO.setFaculty(facultyDao.getById(group.getId()));

        return groupDTO;
    }
}

