package ua.com.foxminded.studenthostel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.dao.GroupDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.Group;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class GroupServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "ABC-1234";

    @Autowired
    @InjectMocks
    GroupService groupService;

    @Autowired
    @InjectMocks
    FacultyService facultyService;

    @Autowired
    @InjectMocks
    CourseNumberService courseNumberService;

    @Mock
    private GroupDao groupDao;
    @Mock
    private FacultyDao facultyDao;
    @Mock
    private CourseNumberDao courseNumberDao;

    @BeforeEach
    public void initMock() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Group group = new Group();

        group.setName(VALID_NAME);
        group.setFacultyId(VALID_ID);
        group.setCourseNumberId(VALID_ID);

        Mockito.when(groupDao.insert(group)).thenReturn(VALID_ID);
        Mockito.when(courseNumberDao.getById(VALID_ID)).thenReturn(new CourseNumber());
        Mockito.when(facultyDao.getById(VALID_ID)).thenReturn(new Faculty());

        Assertions.assertEquals(VALID_ID, groupService.insert(group));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenNameNotValid() {
        Group group = new Group();

        group.setFacultyId(VALID_ID);
        group.setCourseNumberId(VALID_ID);

        group.setName("");
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setName("1");
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setName("A");
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setName("a");
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setName("abc-1234");
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setName("AbC-1234");
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setName("ABC_1234");
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setName("ABC-12345");
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

    }

    @Test
    public void insert_ShouldThrowExceptionWhen_CourseNumberIdNotValid() {
        Group group = new Group();
        group.setName(VALID_NAME);
        group.setFacultyId(VALID_ID);

        group.setCourseNumberId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setCourseNumberId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setCourseNumberId(null);
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));
    }

    @Test
    public void insert_ShouldThrowExceptionWhen_FacultyIdNotValid() {
        Group group = new Group();
        group.setName(VALID_NAME);
        group.setCourseNumberId(VALID_ID);

        group.setFacultyId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setFacultyId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setFacultyId(null);
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));
    }

    @Test
    public void insert_ShouldThrowExceptionWhen_CourseNumberNotExist() {
        Group group = new Group();
        group.setName(VALID_NAME);
        group.setCourseNumberId(VALID_ID);
        group.setFacultyId(VALID_ID);

        Mockito.when(courseNumberDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Mockito.when(facultyDao.getById(VALID_ID)).thenReturn(new Faculty());

        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));
    }

    @Test
    public void insert_ShouldThrowExceptionWhen_FacultyNotExist() {
        Group group = new Group();
        group.setName(VALID_NAME);
        group.setCourseNumberId(VALID_ID);
        group.setFacultyId(VALID_ID);

        Mockito.when(courseNumberDao.getById(VALID_ID)).thenReturn(new CourseNumber());
        Mockito.when(facultyDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> groupService.insert(null));
    }

    @Test
    public void getById_ShouldReturnObject_WhenIdIsValid() {
        Group group = new Group();
        group.setName(VALID_NAME);

        Mockito.when(groupDao.getById(VALID_ID)).thenReturn(group);
        Assertions.assertEquals(group, groupService.getById(VALID_ID));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> groupService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> groupService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> groupService.getById(null));
    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(groupDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> groupService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldReturnResultList_WhenConditionCompleted() {
        Group group = new Group();
        group.setName(VALID_NAME);
        group.setCourseNumberId(VALID_ID);
        group.setFacultyId(VALID_ID);

        List<Group> expectResult = new ArrayList<>();
        expectResult.add(group);

        Mockito.when(groupDao.getAll(1, 10)).thenReturn(expectResult);
        Assertions.assertEquals(expectResult, groupService.getAll(1, 10));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(groupDao.getAll(10, 10)).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NotFoundException.class,
                () -> groupService.getAll(10, 10));
    }

    @Test
    public void update_ShouldReturnTrue_WhenEntryIsUpdated() {
        Group group = new Group();

        group.setId(VALID_ID);
        group.setName(VALID_NAME);
        group.setFacultyId(VALID_ID);
        group.setCourseNumberId(VALID_ID);

        Mockito.when(groupDao.getById(group.getId())).thenReturn(new Group());
        Mockito.when(courseNumberDao.getById(group.getId())).thenReturn(new CourseNumber());
        Mockito.when(facultyDao.getById(group.getId())).thenReturn(new Faculty());

        Mockito.when(groupDao.update(group)).thenReturn(true);
        Assertions.assertTrue(groupService.update(group));
    }

    @Test
    public void update_ShouldThrowException_WhenNameNotValid() {
        Group group = new Group();
        group.setId(VALID_ID);
        group.setFacultyId(VALID_ID);
        group.setCourseNumberId(VALID_ID);

        group.setName("");
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setName("1");
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setName("A");
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setName("a");
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setName("abc-1234");
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setName("AbC-1234");
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setName("ABC_1234");
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setName("ABC-12345");
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));
    }

    @Test
    public void update_ShouldThrowException_WhenIdNotValid() {
        Group group = new Group();

        group.setFacultyId(VALID_ID);
        group.setCourseNumberId(VALID_ID);
        group.setName(VALID_NAME);

        group.setId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setId(null);
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

    }

    @Test
    public void update_ShouldThrowException_WhenEntryNotExist() {
        Group group = new Group();

        group.setFacultyId(VALID_ID);
        group.setCourseNumberId(VALID_ID);
        group.setName(VALID_NAME);
        group.setId(VALID_ID);

        Mockito.when(groupDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));
    }

    @Test
    public void update_ShouldThrowException_WhenFacultyNotExist() {
        Group group = new Group();

        group.setFacultyId(VALID_ID);
        group.setCourseNumberId(VALID_ID);
        group.setName(VALID_NAME);
        group.setId(VALID_ID);

        Mockito.when(facultyDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));
    }

    @Test
    public void update_ShouldThrowException_WhenCourseNumberNotExist() {
        Group group = new Group();

        group.setFacultyId(VALID_ID);
        group.setCourseNumberId(VALID_ID);
        group.setName(VALID_NAME);
        group.setId(VALID_ID);

        Mockito.when(courseNumberDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));
    }

    @Test
    public void update_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> groupService.update(null));
    }

    @Test
    public void deleteById_ShouldReturnTrue_WhenEntryIsDeleted() {
        Mockito.when(groupDao.deleteById(VALID_ID)).thenReturn(true);
        Mockito.when(groupDao.getById(VALID_ID)).thenReturn(new Group());

        Assertions.assertTrue(groupService.deleteById(VALID_ID));
    }

    @Test
    public void deleteById_ShouldThrowException_WhenIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> groupService.deleteById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> groupService.deleteById(BigInteger.ZERO));
    }

    @Test
    public void deleteById_ShouldThrowException_WhenEntryNotExist() {
        Mockito.when(groupDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> groupService.deleteById(VALID_ID));
    }
}
