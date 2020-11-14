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
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.dao.EquipmentDao;
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.dao.GroupDao;
import ua.com.foxminded.studenthostel.dao.RoomDao;
import ua.com.foxminded.studenthostel.dao.StudentDao;
import ua.com.foxminded.studenthostel.dao.TaskDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.Task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@SpringBootTest
class StudentServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;

    public static final String VALID_NAME = "Validname";
    public static final int VALID_DEBT = 20;
    public static final int VALID_STUDENTS_COUNT = 2;

    @InjectMocks
    @Autowired
    StudentService studentService;

    @InjectMocks
    @Autowired
    private RoomService roomService;

    @Autowired
    @InjectMocks
    private GroupService groupService;

    @Autowired
    @InjectMocks
    private TaskService taskService;

    @Autowired
    @InjectMocks
    private CourseNumberService courseNumberService;

    @Autowired
    @InjectMocks
    private FacultyService facultyService;

    @Autowired
    @InjectMocks
    private FloorService floorService;

    @Mock
    private StudentDao studentDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private RoomDao roomDao;
    @Mock
    private EquipmentDao equipmentDao;
    @Mock
    private TaskDao taskDao;
    @Mock
    private FloorDao floorDao;
    @Mock
    private CourseNumberDao courseNumberDao;
    @Mock
    private FacultyDao facultyDao;

    @BeforeEach
    public void initMock() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Student student = new Student(null, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        Mockito.when(studentDao.insert(student)).thenReturn(VALID_ID);
        Mockito.when(roomDao.getById(VALID_ID)).thenReturn(getRoom(VALID_ID));

        Assertions.assertEquals(VALID_ID, studentService.insert(student));

    }

    @Test
    public void insert_ShouldThrowException_WhenFirstNameNotValid() {
        Student student = new Student(null, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        student.setFirstName("");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setFirstName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setFirstName("Not valid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setFirstName("NotValid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setFirstName("n");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setFirstName("N");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setFirstName("123");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setFirstName("");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setFirstName(" ");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setFirstName("   ");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setFirstName(null);
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));
    }

    @Test
    public void insert_ShouldThrowException_WhenLastNameNotValid() {

        Student student = new Student(null, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        student.setLastName("");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setLastName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setLastName("Not valid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setLastName("NotValid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setLastName("n");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setLastName("N");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setLastName("123");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setLastName("");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setLastName(" ");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setLastName("   ");
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setLastName(null);
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));
    }

    @Test
    public void insert_ShouldThrowException_WhenHoursDebtNotValid() {
        Student student = new Student(null, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        student.setHoursDebt(-1);
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setHoursDebt(41);
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));
    }

    @Test
    public void insert_ShouldThrowException_WhenRoomIdNotValid() {
        Student student = new Student(null, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        student.setRoom(getRoom(ZERO_ID));
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setRoom(getRoom(NEGATIVE_ID));
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setRoom(getRoom(null));
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));
    }

    @Test
    public void insert_ShouldThrowExceptionWhen_GroupIdNotValid() {
        Student student = new Student(null, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        student.setGroup(getGroup(ZERO_ID));
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setGroup(getGroup(NEGATIVE_ID));
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));

        student.setGroup(getGroup(null));
        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));
    }

    @Test
    public void insert_ShouldThrowException_WhenGroupNotExist() {
        Student student = new Student(null, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));
        Mockito.when(groupDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));
    }

    @Test
    public void insert_ShouldThrowException_WhenRoomNotExist() {
        Student student = new Student(null, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));
        Mockito.when(roomDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));
    }

    @Test
    public void getById_ShouldReturnObject_WhenIdIsValid() {
        Student student = new Student(VALID_ID, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(student);
        Assertions.assertEquals(student, studentService.getById(VALID_ID));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> studentService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> studentService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> roomService.getById(null));
    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(studentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> studentService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldReturnResultList_WhenConditionCompleted() {
        Student student = new Student(VALID_ID, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        List<Student> expectResult = new ArrayList<>();
        expectResult.add(student);

        Mockito.when(studentDao.getAll(1, 10)).thenReturn(expectResult);
        Assertions.assertEquals(expectResult, studentService.getAll(1, 10));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(studentDao.getAll(10, 10)).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NotFoundException.class,
                () -> studentService.getAll(10, 10));
    }

    @Test
    public void getAllByFloor_ShouldThrowException_WhenFloorIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByFloor(ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByFloor(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByFloor(null));
    }

    @Test
    public void getAllByFloor_ShouldThrowException_WhenFloorNotExist() {
        Mockito.when(floorDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByFloor(VALID_ID));
    }

    @Test
    public void getAllByFloor_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(studentDao.getAllByFloor(VALID_ID)).thenReturn(Collections.emptyList());

        Assertions.assertThrows(NotFoundException.class,
                () -> studentService.getAllByFloor(VALID_ID));
    }

    @Test
    public void getAllByFaculty_ShouldThrowException_WhenFacultyIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByFaculty(ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByFaculty(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByFaculty(null));
    }

    @Test
    public void getAllByFaculty_ShouldThrowException_WhenFacultyNotExist() {
        Mockito.when(facultyDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByFaculty(VALID_ID));
    }

    @Test
    public void getAllByFaculty_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(studentDao.getAllByFaculty(VALID_ID)).thenReturn(Collections.emptyList());

        Assertions.assertThrows(NotFoundException.class,
                () -> studentService.getAllByFaculty(VALID_ID));
    }

    @Test
    public void getAllByCourse_ShouldThrowException_WhenCourseIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByCourse(ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByCourse(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByCourse(null));
    }

    @Test
    public void getAllByCourse_ShouldThrowException_WhenCourseNotExist() {
        Mockito.when(courseNumberDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByCourse(VALID_ID));
    }

    @Test
    public void getAllByCourse_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(studentDao.getAllByCourse(VALID_ID)).thenReturn(Collections.emptyList());

        Assertions.assertThrows(NotFoundException.class,
                () -> studentService.getAllByCourse(VALID_ID));
    }

    @Test
    public void getAllWithDebitByGroup_ShouldThrowException_WhenGroupIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllWithDebitByGroup(ZERO_ID, 10));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllWithDebitByGroup(NEGATIVE_ID, 10));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllWithDebitByGroup(null, 10));
    }

    @Test
    public void getAllWithDebitByGroup_ShouldThrowException_WhenGroupNotExist() {
        Mockito.when(groupDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllWithDebitByGroup(VALID_ID, 20));
    }

    @Test
    public void getAllWithDebitByGroup_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(studentDao.getAllWithDebitByGroup(VALID_ID,
                10)).thenReturn(Collections.emptyList());

        Assertions.assertThrows(NotFoundException.class,
                () -> studentService.getAllWithDebitByGroup(VALID_ID, 10));

    }

    @Test
    public void changeRoom_ShouldThrowException_WhenIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeRoom(ZERO_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeRoom(NEGATIVE_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeRoom(null, VALID_ID));


        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeRoom(VALID_ID, ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeRoom(VALID_ID, NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeRoom(VALID_ID, null));
    }

    @Test
    public void changeRoom_ShouldThrowException_WhenStudentNotExist() {

        Mockito.when(studentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Mockito.when(roomDao.getById(VALID_ID)).thenReturn(new Room());

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeRoom(VALID_ID, VALID_ID));
    }

    @Test
    public void changeRoom_ShouldThrowException_WhenRoomNotExist() {
        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(roomDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeRoom(VALID_ID, VALID_ID));
    }

    @Test
    public void changeDebt_ShouldThrowException_WhenDebitNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeDebt(-1, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeDebt(41, VALID_ID));
    }

    @Test
    public void changeDebt_ShouldThrowException_WhenStudentNotExist() {
        Mockito.when(studentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeDebt(10, VALID_ID));
    }

    @Test
    public void update_ShouldThrowException_WhenFirstNameNotValid() {
        Student student = new Student(VALID_ID, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        student.setFirstName("");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setFirstName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setFirstName("Not valid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setFirstName("NotValid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setFirstName("n");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setFirstName("N");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setFirstName("123");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setFirstName("");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setFirstName(" ");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setFirstName("   ");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setFirstName(null);
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));
    }

    @Test
    public void update_ShouldThrowException_WhenLastNameNotValid() {

        Student student = new Student(VALID_ID, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        student.setLastName("");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setLastName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setLastName("Not valid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setLastName("NotValid");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setLastName("n");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setLastName("N");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setLastName("123");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setLastName("");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setLastName(" ");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setLastName("   ");
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setLastName(null);
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));
    }

    @Test
    public void update_ShouldThrowException_WhenHoursDebtNotValid() {
        Student student = new Student(VALID_ID, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        student.setHoursDebt(-1);
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setHoursDebt(41);
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));
    }

    @Test
    public void update_ShouldThrowException_WhenRoomIdNotValid() {
        Student student = new Student(VALID_ID, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        student.setRoom(getRoom(ZERO_ID));
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setRoom(getRoom(NEGATIVE_ID));
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setRoom(getRoom(null));
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));
    }

    @Test
    public void update_ShouldThrowExceptionWhen_GroupIdNotValid() {
        Student student = new Student(VALID_ID, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        student.setGroup(getGroup(ZERO_ID));
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setGroup(getGroup(NEGATIVE_ID));
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));

        student.setGroup(getGroup(null));
        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));
    }

    @Test
    public void update_ShouldThrowException_WhenStudentNotExist() {
        Student student = new Student(VALID_ID, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));
        Mockito.when(studentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));
    }

    @Test
    public void update_ShouldThrowException_WhenGroupNotExist() {
        Student student = new Student(VALID_ID, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));
        Mockito.when(groupDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));
    }

    @Test
    public void update_ShouldThrowException_WhenRoomNotExist() {
        Student student = new Student(VALID_ID, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));
        Mockito.when(roomDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));
    }

    @Test
    public void acceptHoursAndUpdate_ShouldThrowException_WhenIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> studentService.acceptTaskAndUpdateHours(VALID_ID, ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.acceptTaskAndUpdateHours(VALID_ID, NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.acceptTaskAndUpdateHours(VALID_ID, null));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.acceptTaskAndUpdateHours(ZERO_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.acceptTaskAndUpdateHours(NEGATIVE_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.acceptTaskAndUpdateHours(null, VALID_ID));
    }

    @Test
    public void acceptHoursAndUpdate_ShouldThrowException_WhenStudentNotExist() {
        Mockito.when(studentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Mockito.when(taskDao.getById(VALID_ID)).thenReturn(new Task());

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.acceptTaskAndUpdateHours(VALID_ID, VALID_ID));
    }

    @Test
    public void acceptHoursAndUpdate_ShouldThrowException_WhenTaskNotExist() {
        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(taskDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.acceptTaskAndUpdateHours(VALID_ID, VALID_ID));
    }

    @Test
    public void acceptHoursAndUpdate_ShouldThrowException_WhenStudentTaskRelation_NotExist() {
        Mockito.when(taskDao.getById(VALID_ID)).thenReturn(new Task());
        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(taskDao.isStudentTaskRelationExist(VALID_ID, VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.acceptTaskAndUpdateHours(VALID_ID, VALID_ID));
    }


    @Test
    public void deleteById_ShouldThrowException_WhenIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> studentService.deleteById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.deleteById(BigInteger.ZERO));

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.deleteById(null));
    }

    @Test
    public void deleteById_ShouldThrowException_WhenEntryNotExist() {
        Mockito.when(studentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.deleteById(VALID_ID));
    }

    Room getRoom(BigInteger id) {
        Room room = new Room();
        room.setName("name");
        room.setId(id);

        return room;
    }

    Group getGroup(BigInteger id) {
        Group group = new Group();
        group.setName("name");
        group.setId(id);
        return group;
    }
}
