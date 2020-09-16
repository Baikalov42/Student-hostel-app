package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.dao.GroupDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
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


    public BigInteger insert(Group group) throws ValidationException {

        validator.validateEntity(group);
        return groupDao.insert(group);
    }


    public GroupDTO getById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        Group group = groupDao.getById(id);
        return getDTO(group);
    }

    public List<GroupDTO> getAll(long limit, long offset) throws ValidationException {
        long countOfEntries = groupDao.getEntriesCount().longValue();

        if (countOfEntries <= offset) {
            throw new ValidationException("offset is greater than the number of entries");
        }

        List<Group> groups = groupDao.getAll(limit, offset);
        List<GroupDTO> groupDTOS = new ArrayList<>();

        for (Group group : groups) {
            groupDTOS.add(getDTO(group));
        }
        return groupDTOS;
    }


    public boolean update(Group group) throws ValidationException {

        validator.validateEntity(group);
        validator.validateId(group.getId());
        validateForExist(group.getId());

        return groupDao.update(group);
    }

    public boolean deleteById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        validateForExist(id);

        return groupDao.deleteById(id);
    }

    protected void validateForExist(BigInteger id) throws ValidationException {
        try {
            groupDao.getById(id);
        } catch (DaoException ex) {
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

