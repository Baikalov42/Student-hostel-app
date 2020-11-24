package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ua.com.foxminded.studenthostel.repository.GroupRepository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Group;

import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;

import java.util.List;

@Service
public class GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private GroupRepository groupRepository;
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

        try {
            return groupRepository.save(group).getId();

        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", group, ex);
            throw new DaoException("Insertion error : " + group, ex);
        }
    }

    public Group getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        return groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + id));
    }

    public List<Group> getAll(int pageNumber) {
        LOGGER.debug("getting all, pageNumber = {}, pageSize = {} ", pageNumber, PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<Group> result = groupRepository.findAll(pageable).getContent();

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, pageNumber = {} ", pageNumber);
            throw new NotFoundException("Result with pageNumber =" + pageNumber + " is empty");
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
        try {
            return groupRepository.save(group);

        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", group, ex);
            throw new DaoException("Updating error: " + group, ex);
        }
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        try {
            groupRepository.deleteById(id);
        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);

        if (!groupRepository.existsById(id)) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist");
        }
    }
}

