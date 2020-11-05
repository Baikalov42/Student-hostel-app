package ua.com.foxminded.studenthostel.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.studenthostel.config.WebConfig;
import ua.com.foxminded.studenthostel.controllers.handlers.ExceptionController;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.service.CourseNumberService;
import ua.com.foxminded.studenthostel.service.FacultyService;
import ua.com.foxminded.studenthostel.service.FloorService;
import ua.com.foxminded.studenthostel.service.StudentService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.studenthostel.controllers.GroupControllerTest.getGroupFull;
import static ua.com.foxminded.studenthostel.controllers.RoomControllerTest.getRoomFull;
import static ua.com.foxminded.studenthostel.controllers.FloorControllerTest.getFloor;
import static ua.com.foxminded.studenthostel.controllers.FacultyControllerTest.getFaculty;
import static ua.com.foxminded.studenthostel.controllers.CourseNumberControllerTest.getCourseNumber;


@SpringJUnitWebConfig(WebConfig.class)
class StudentControllerTest {

    private static final BigInteger ONE = BigInteger.ONE;
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private StudentController studentController;

    @Autowired
    private ExceptionController exceptionController;

    @Mock
    private StudentService studentService;
    @Mock
    private FloorService floorService;
    @Mock
    private FacultyService facultyService;
    @Mock
    private CourseNumberService courseNumberService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(studentController, exceptionController)
                .build();
    }

    @Test
    public void insert_GET_ShouldReturnInsertFormView_WhenConditionComplete() throws Exception {
        mockMvc.perform(get("/students/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/student-insert"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfMessage_WhenEntryInserted() throws Exception {
        Mockito.when(studentService.insert(getNullIdStudent())).thenReturn(ONE);

        mockMvc.perform(post("/students/insert")
                .param("firstName", "Firstname")
                .param("lastName", "Lastname")
                .param("room.id", ONE.toString())
                .param("group.id", ONE.toString())
                .param("hoursDebt", String.valueOf(10)))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Adding completed."))
                .andExpect(model().attribute("id", "New ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfError_WhenEntryNotInserted() throws Exception {
        Mockito.when(studentService.insert(getNullIdStudent())).thenThrow(DaoException.class);

        mockMvc.perform(post("/students/insert")
                .param("firstName", "Firstname")
                .param("lastName", "Lastname")
                .param("room.id", ONE.toString())
                .param("group.id", ONE.toString())
                .param("hoursDebt", String.valueOf(10)))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(studentService.getById(BigInteger.ONE)).thenReturn(getStudentFull());

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/student-info"))
                .andExpect(model().attribute("student", getStudentFull()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(studentService.getById(BigInteger.ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Student> students = Collections.singletonList(getStudent());
        Mockito.when(studentService.getAll(0, 10)).thenReturn(students);

        mockMvc.perform(get("/students/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-list"))
                .andExpect(model().attribute("students", students));
    }

    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(studentService.getAll(0, 10)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAllByFloor_ShouldReturnViewListOfFloors_WhenFloorsExists() throws Exception {
        List<Floor> floors = Collections.singletonList(getFloor());
        Mockito.when(floorService.getAll(0, 10))
                .thenReturn(floors);

        mockMvc.perform(get("/students/byFloor/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-by-floor"))
                .andExpect(model().attribute("floors", floors));
    }

    @Test
    public void getAllByFloor_ShouldReturnViewOfError_WhenFloorsNotExists() throws Exception {

        Mockito.when(floorService.getAll(0, 10))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/byFloor/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAllByFloorResult_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Student> students = Collections.singletonList(getStudentFull());
        Mockito.when(studentService.getAllByFloor(getFloor().getId()))
                .thenReturn(students);

        mockMvc.perform(get("/students/byFloor/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-list"))
                .andExpect(model().attribute("students", students));
    }

    @Test
    public void getAllByFloorResult_ShouldReturnViewOfError_WhenEntriesNotExists() throws Exception {

        Mockito.when(studentService.getAllByFloor(getFloor().getId()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/byFloor/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAllByFaculty_ShouldReturnViewListOfFaculties_WhenFacultiesExists() throws Exception {
        List<Faculty> faculties = Collections.singletonList(getFaculty());
        Mockito.when(facultyService.getAll(0, 10))
                .thenReturn(faculties);

        mockMvc.perform(get("/students/byFaculty/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-by-faculty"))
                .andExpect(model().attribute("faculties", faculties));
    }

    @Test
    public void getAllByFaculty_ShouldReturnViewOfError_WhenFacultiesNotExists() throws Exception {

        Mockito.when(facultyService.getAll(0, 10))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/byFaculty/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAllByFacultyResult_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Student> students = Collections.singletonList(getStudentFull());
        Mockito.when(studentService.getAllByFaculty(getFaculty().getId()))
                .thenReturn(students);

        mockMvc.perform(get("/students/byFaculty/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-list"))
                .andExpect(model().attribute("students", students));
    }

    @Test
    public void getAllByFacultyResult_ShouldReturnViewOfError_WhenEntriesNotExists() throws Exception {

        Mockito.when(studentService.getAllByFaculty(getFaculty().getId()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/byFaculty/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAllByCourse_ShouldReturnViewListOfCourseNumbers_WhenCourseNumberExists() throws Exception {
        List<CourseNumber> courseNumbers = Collections.singletonList(getCourseNumber());
        Mockito.when(courseNumberService.getAll(0, 10))
                .thenReturn(courseNumbers);

        mockMvc.perform(get("/students/byCourse/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-by-course"))
                .andExpect(model().attribute("courseNumbers", courseNumbers));
    }

    @Test
    public void getAllByCourse_ShouldReturnViewOfError_WhenCourseNumbersNotExists() throws Exception {

        Mockito.when(courseNumberService.getAll(0, 10))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/byCourse/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAllByCourseResult_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Student> students = Collections.singletonList(getStudentFull());
        Mockito.when(studentService.getAllByCourse(getCourseNumber().getId()))
                .thenReturn(students);

        mockMvc.perform(get("/students/byCourse/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-list"))
                .andExpect(model().attribute("students", students));
    }

    @Test
    public void getAllByCourseResult_ShouldReturnViewOfError_WhenEntriesNotExists() throws Exception {

        Mockito.when(studentService.getAllByCourse(getCourseNumber().getId()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/byCourse/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAllByGroupWithDebtExample_ShouldReturnViewOfExample() throws Exception {
        mockMvc.perform(get("/students/byGroupWithDebtExample"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-by-group-with-debt-example"));
    }

    @Test
    public void getAllByGroupWithDebt_ShouldReturnViewOfResult_WhenResultNotEmpty() throws Exception {

        List<Student> students = Collections.singletonList(getStudentFull());
        Mockito.when(studentService.getAllWithDebitByGroup(getCourseNumber().getId(), 30))
                .thenReturn(students);

        mockMvc.perform(get("/students/byGroupWithDebt/group/1/debt/30"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-list"))
                .andExpect(model().attribute("students", students));
    }

    @Test
    public void update_GET_ShouldReturnUpdateFormView_WhenConditionComplete() throws Exception {
        Mockito.when(studentService.getById(ONE)).thenReturn(getStudent());

        mockMvc.perform(get("/students/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("students/student-update"))
                .andExpect(model().attribute("student", getStudent()));
    }

    @Test
    public void update_GET_ShouldReturnErrorView_WhenEntryNotExist() throws Exception {
        Mockito.when(studentService.getById(ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfMessage_WhenEntryUpdated() throws Exception {
        Mockito.when(studentService.update(getStudent())).thenReturn(getStudentFull());

        mockMvc.perform(post("/students/update/1")
                .param("id", ONE.toString())
                .param("firstName", "Firstname")
                .param("lastName", "Lastname")
                .param("room.id", ONE.toString())
                .param("group.id", ONE.toString())
                .param("hoursDebt", String.valueOf(10)))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Updating complete"))
                .andExpect(model().attribute("id", "Updated ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfError_WhenEntryNotUpdated() throws Exception {
        Mockito.when(studentService.update(getStudent())).thenThrow(DaoException.class);

        mockMvc.perform(post("/students/update/1")
                .param("id", ONE.toString())
                .param("firstName", "Firstname")
                .param("lastName", "Lastname")
                .param("roomId", ONE.toString())
                .param("groupId", ONE.toString())
                .param("hoursDebt", String.valueOf(10)))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void changeRoom_GET_ShouldReturnUpdateFormView_WhenConditionComplete() throws Exception {
        Mockito.when(studentService.getById(ONE)).thenReturn(getStudent());

        mockMvc.perform(get("/students/update-room/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("students/student-update-room"))
                .andExpect(model().attribute("student", getStudent()));
    }

    @Test
    public void changeRoom_POST_ShouldReturnViewOfMessage_WhenEntryUpdated() throws Exception {
        Mockito.when(studentService.update(getStudentFull())).thenReturn(getStudentFull());

        mockMvc.perform(post("/students/update-room")
                .param("id", ONE.toString())
                .param("room.id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Updating complete"))
                .andExpect(model().attribute("id", "Student ID = " + ONE + ", updated room on ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void changeRoom_POST_ShouldReturnViewOfError_WhenEntryNotUpdated() throws Exception {
        Mockito.when(studentService.changeRoom(ONE, ONE)).thenThrow(DaoException.class);

        mockMvc.perform(post("/students/update-room")
                .param("id", ONE.toString())
                .param("roomId", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void changeDebt_GET_ShouldReturnUpdateFormView_WhenConditionComplete() throws Exception {
        Mockito.when(studentService.getById(ONE)).thenReturn(getStudent());

        mockMvc.perform(get("/students/update-debt/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("students/student-update-debt"))
                .andExpect(model().attribute("student", getStudent()));
    }

    @Test
    public void changeDebt_POST_ShouldReturnViewOfMessage_WhenEntryUpdated() throws Exception {

        mockMvc.perform(post("/students/update-debt")
                .param("id", ONE.toString())
                .param("hoursDebt", String.valueOf(10)))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Updating complete"))
                .andExpect(model().attribute("id", "Student ID = " + ONE + ", updated debt on: " + 10))
                .andExpect(view().name("message"));
    }

    @Test
    public void changeDebt_POST_ShouldReturnViewOfError_WhenEntryNotUpdated() throws Exception {
        Mockito.when(studentService.changeDebt(10, ONE)).thenThrow(DaoException.class);

        mockMvc.perform(post("/students/update-debt")
                .param("id", ONE.toString())
                .param("hoursDebt", String.valueOf(10)))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void delete_ShouldReturnViewOfMessage_WhenEntryDeleted() throws Exception {
        Mockito.doNothing().when(studentService).deleteById(ONE);

        mockMvc.perform(post("/students/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Deleting complete"))
                .andExpect(model().attribute("id", "Deleted student id = " + ONE))
                .andExpect(view().name("message"));
    }

    static Student getNullIdStudent() {

        Group group = new Group();
        group.setId(ONE);

        Room room = new Room();
        room.setId(ONE);

        Student student = new Student();

        student.setFirstName("Firstname");
        student.setLastName("Lastname");
        student.setGroup(group);
        student.setRoom(room);
        student.setHoursDebt(10);

        return student;
    }

    static Student getStudent() {
        Student student = getNullIdStudent();
        student.setId(BigInteger.ONE);

        return student;
    }

    static Student getStudentFull() {
        Student student = getStudent();

        student.setId(BigInteger.ONE);

        student.setFirstName(student.getFirstName());
        student.setLastName(student.getLastName());

        student.setGroup(getGroupFull());
        student.setRoom(getRoomFull());

        student.setHoursDebt(10);
        student.setTasks(Collections.emptySet());
        student.setEquipments(Collections.emptySet());

        return student;
    }
}
