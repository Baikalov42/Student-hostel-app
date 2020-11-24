package ua.com.foxminded.studenthostel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ua.com.foxminded.studenthostel.repository.CourseNumberRepository;
import ua.com.foxminded.studenthostel.repository.FacultyRepository;
import ua.com.foxminded.studenthostel.repository.GroupRepository;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.Group;

import java.math.BigInteger;
import java.util.Optional;

@SpringBootTest
class GroupServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "ABC-1234";
    public static final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

    @Autowired
    @InjectMocks
    private GroupService groupService;

    @Autowired
    @InjectMocks
    private FacultyService facultyService;

    @Autowired
    @InjectMocks
    private CourseNumberService courseNumberService;

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private FacultyRepository facultyRepository;
    @Mock
    private CourseNumberRepository courseNumberRepository;

    @BeforeEach
    public void initMock() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Group toDB = new Group();
        toDB.setName(VALID_NAME);
        toDB.setCourseNumber(getCourse(VALID_ID));
        toDB.setFaculty(getFaculty(VALID_ID));

        Group fromDB = getGroup(VALID_ID, VALID_NAME);

        Mockito.when(groupRepository.save(toDB)).thenReturn(fromDB);
        Mockito.when(courseNumberRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(facultyRepository.existsById(VALID_ID)).thenReturn(true);

        Assertions.assertEquals(VALID_ID, groupService.insert(toDB));
    }


    @Test
    public void insert_ShouldThrowExceptionWhenNameNotValid() {
        Group group = new Group();

        group.setFaculty(getFaculty(VALID_ID));
        group.setCourseNumber(getCourse(VALID_ID));

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
        group.setFaculty(getFaculty(VALID_ID));

        group.setCourseNumber(getCourse(ZERO_ID));
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setCourseNumber(getCourse(NEGATIVE_ID));
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setCourseNumber(getCourse(null));
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));
    }

    @Test
    public void insert_ShouldThrowExceptionWhen_FacultyIdNotValid() {
        Group group = new Group();
        group.setName(VALID_NAME);
        group.setCourseNumber(getCourse(VALID_ID));

        group.setFaculty(getFaculty(ZERO_ID));
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setFaculty(getFaculty(NEGATIVE_ID));
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));

        group.setFaculty(getFaculty(null));
        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));
    }

    @Test
    public void insert_ShouldThrowExceptionWhen_CourseNumberNotExist() {
        Group group = new Group();
        group.setName(VALID_NAME);
        group.setFaculty(getFaculty(VALID_ID));
        group.setCourseNumber(getCourse(VALID_ID));

        Mockito.when(courseNumberRepository.existsById(VALID_ID)).thenReturn(false);
        Mockito.when(facultyRepository.existsById(VALID_ID)).thenReturn(true);

        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));
    }

    @Test
    public void insert_ShouldThrowExceptionWhen_FacultyNotExist() {
        Group group = new Group();
        group.setName(VALID_NAME);
        group.setFaculty(getFaculty(VALID_ID));
        group.setCourseNumber(getCourse(VALID_ID));

        Mockito.when(courseNumberRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(facultyRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class, () -> groupService.insert(group));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> groupService.insert(null));
    }

    @Test
    public void getById_ShouldReturnObject_WhenIdIsValid() {
        Group fromDB = getGroup(VALID_ID, VALID_NAME);

        Mockito.when(groupRepository.findById(VALID_ID)).thenReturn(Optional.of(fromDB));
        Assertions.assertEquals(fromDB, groupService.getById(VALID_ID));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> groupService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> groupService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> groupService.getById(null));
    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(groupRepository.findById(VALID_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> groupService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(groupRepository.findAll(PAGEABLE)).thenReturn(Page.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> groupService.getAll(0));
    }


    @Test
    public void update_ShouldThrowException_WhenNameNotValid() {
        Group group = getGroup(VALID_ID, VALID_NAME);

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
        Group group = getGroup(VALID_ID, VALID_NAME);

        group.setId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

        group.setId(null);
        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));

    }

    @Test
    public void update_ShouldThrowException_WhenEntryNotExist() {
        Group group = getGroup(VALID_ID, VALID_NAME);

        Mockito.when(groupRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));
    }

    @Test
    public void update_ShouldThrowException_WhenFacultyNotExist() {
        Group group = getGroup(VALID_ID, VALID_NAME);

        Mockito.when(facultyRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));
    }

    @Test
    public void update_ShouldThrowException_WhenCourseNumberNotExist() {
        Group group = getGroup(VALID_ID, VALID_NAME);

        Mockito.when(courseNumberRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class, () -> groupService.update(group));
    }

    @Test
    public void update_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> groupService.update(null));
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
        Mockito.when(groupRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> groupService.deleteById(VALID_ID));
    }

    private Group getGroup(BigInteger id, String name) {
        Group group = new Group();
        group.setId(id);
        group.setName(name);
        group.setFaculty(getFaculty(VALID_ID));
        group.setCourseNumber(getCourse(VALID_ID));
        return group;
    }

    private CourseNumber getCourse(BigInteger id) {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("Coursename");
        courseNumber.setId(id);

        return courseNumber;
    }

    private Faculty getFaculty(BigInteger id) {
        Faculty faculty = new Faculty();
        faculty.setName("Facultyname");
        faculty.setId(id);

        return faculty;
    }
}
