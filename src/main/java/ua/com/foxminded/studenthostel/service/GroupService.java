package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.dao.GroupDao;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.dto.GroupDTO;
import ua.com.foxminded.studenthostel.service.utils.PatternValidator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    private static final String NAME_PATTERN = "[A-Z]{3}[-][0-9]{4}";

    @Autowired
    GroupDao groupDao;
    @Autowired
    FacultyDao facultyDao;
    @Autowired
    CourseNumberDao courseNumberDao;


    public BigInteger insert(Group group) throws ValidationException {

        PatternValidator.validateName(group.getName(), NAME_PATTERN);
        return groupDao.insert(group);
    }


    public GroupDTO getById(BigInteger id) {
        if (id == null || id.longValue() == 0) {
            throw new IllegalArgumentException();
        }
        Group group = groupDao.getById(id);
        return getDTO(group);
    }

    public List<GroupDTO> getAll(long limit, long offset) {

        List<Group> groups = groupDao.getAll(limit, offset);
        List<GroupDTO> groupsDTO = new ArrayList<>();

        for (Group group : groups) {
            groupsDTO.add(getDTO(group));
        }
        return groupsDTO;
    }


    public boolean update(Group group) throws ValidationException {
        PatternValidator.validateName(group.getName(), NAME_PATTERN);
        return groupDao.update(group);
    }

    public boolean deleteById(BigInteger id) {
        return groupDao.deleteById(id);
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

