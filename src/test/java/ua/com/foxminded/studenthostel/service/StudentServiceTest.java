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
import ua.com.foxminded.studenthostel.repository.EquipmentRepository;
import ua.com.foxminded.studenthostel.repository.FacultyRepository;
import ua.com.foxminded.studenthostel.repository.FloorRepository;
import ua.com.foxminded.studenthostel.repository.GroupRepository;
import ua.com.foxminded.studenthostel.repository.RoomRepository;
import ua.com.foxminded.studenthostel.repository.StudentRepository;
import ua.com.foxminded.studenthostel.repository.TaskRepository;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.Student;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@SpringBootTest
class StudentServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;

    public static final String VALID_NAME = "Validname";
    public static final int VALID_DEBT = 20;
    public static final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.Direction.ASC, "id");


    @InjectMocks
    @Autowired
    private StudentService studentService;

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
    private StudentRepository studentRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private FloorRepository floorRepository;
    @Mock
    private CourseNumberRepository courseNumberRepository;
    @Mock
    private FacultyRepository facultyRepository;

    @BeforeEach
    public void initMock() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Room room = getRoom(VALID_ID);
        Set<Student> students = new HashSet<>();
        room.setStudents(students);

        Student toDB = new Student(null, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));
        Student fromDB = getValidStudent();

        Mockito.when(studentRepository.save(toDB)).thenReturn(fromDB);

        Mockito.when(groupRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(roomRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(roomRepository.findById(VALID_ID)).thenReturn(Optional.of(room));

        Assertions.assertEquals(VALID_ID, studentService.insert(toDB));

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

        Mockito.when(groupRepository.existsById(VALID_ID)).thenReturn(false);
        Mockito.when(roomRepository.existsById(VALID_ID)).thenReturn(true);

        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(student));
    }

    @Test
    public void insert_ShouldThrowException_WhenRoomNotExist() {
        Student toDB = new Student(null, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        Mockito.when(groupRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(roomRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class, () -> studentService.insert(toDB));
    }

    @Test
    public void getById_ShouldReturnObject_WhenIdIsValid() {
        Student fromDB = getValidStudent();

        Mockito.when(studentRepository.findById(VALID_ID)).thenReturn(Optional.of(fromDB));
        Assertions.assertEquals(fromDB, studentService.getById(VALID_ID));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> studentService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> studentService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> roomService.getById(null));
    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(studentRepository.findById(VALID_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> studentService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(studentRepository.findAll(PAGEABLE)).thenReturn(Page.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> studentService.getAll(0));
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
        Mockito.when(floorRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByFloor(VALID_ID));
    }

    @Test
    public void getAllByFloor_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(floorRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(studentRepository.getAllByFloor(VALID_ID)).thenReturn(Collections.emptyList());

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
        Mockito.when(facultyRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByFaculty(VALID_ID));
    }

    @Test
    public void getAllByFaculty_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(facultyRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(studentRepository.getAllByFaculty(VALID_ID)).thenReturn(Collections.emptyList());

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
        Mockito.when(courseNumberRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllByCourse(VALID_ID));
    }

    @Test
    public void getAllByCourse_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(courseNumberRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(studentRepository.getAllByCourse(VALID_ID)).thenReturn(Collections.emptyList());

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
        Mockito.when(groupRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.getAllWithDebitByGroup(VALID_ID, 20));
    }

    @Test
    public void getAllWithDebitByGroup_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(groupRepository.existsById(VALID_ID)).thenReturn(true);

        Mockito.when(studentRepository.getAllWithDebitByGroup(VALID_ID,
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

        Mockito.when(studentRepository.existsById(VALID_ID)).thenReturn(false);
        Mockito.when(roomRepository.existsById(VALID_ID)).thenReturn(true);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.changeRoom(VALID_ID, VALID_ID));
    }

    @Test
    public void changeRoom_ShouldThrowException_WhenRoomNotExist() {
        Mockito.when(studentRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(roomRepository.existsById(VALID_ID)).thenReturn(false);

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
        Mockito.when(studentRepository.existsById(VALID_ID)).thenReturn(false);

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
        Student student = getValidStudent();
        Mockito.when(studentRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));
    }

    @Test
    public void update_ShouldThrowException_WhenGroupNotExist() {
        Student student = getValidStudent();
        Mockito.when(groupRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class, () -> studentService.update(student));
    }

    @Test
    public void update_ShouldThrowException_WhenRoomNotExist() {
        Student student = getValidStudent();
        Mockito.when(roomRepository.existsById(VALID_ID)).thenReturn(false);

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
        Mockito.when(groupRepository.existsById(VALID_ID)).thenReturn(false);
        Mockito.when(taskRepository.existsById(VALID_ID)).thenReturn(true);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.acceptTaskAndUpdateHours(VALID_ID, VALID_ID));
    }

    @Test
    public void acceptHoursAndUpdate_ShouldThrowException_WhenTaskNotExist() {
        Mockito.when(groupRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(taskRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.acceptTaskAndUpdateHours(VALID_ID, VALID_ID));
    }

    @Test
    public void acceptHoursAndUpdate_ShouldThrowException_WhenStudentTaskRelation_NotExist() {
        Mockito.when(groupRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(taskRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(taskRepository.isStudentTaskRelationExist(VALID_ID, VALID_ID)).thenReturn(false);

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
        Mockito.when(studentRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> studentService.deleteById(VALID_ID));
    }

    private Student getValidStudent() {
        Student student = new Student(
                VALID_ID, VALID_NAME, VALID_NAME, VALID_DEBT, getGroup(VALID_ID), getRoom(VALID_ID));

        return student;
    }

    private Room getRoom(BigInteger id) {
        Room room = new Room();
        room.setName("AB-0001");
        room.setId(id);

        return room;
    }

    private Group getGroup(BigInteger id) {
        Group group = new Group();
        group.setName("SEN-1111");
        group.setId(id);
        return group;
    }
}
