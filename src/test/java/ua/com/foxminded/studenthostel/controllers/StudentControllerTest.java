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
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.dto.StudentDTO;
import ua.com.foxminded.studenthostel.service.CourseNumberService;
import ua.com.foxminded.studenthostel.service.FacultyService;
import ua.com.foxminded.studenthostel.service.FloorService;
import ua.com.foxminded.studenthostel.service.StudentService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static ua.com.foxminded.studenthostel.controllers.GroupControllerTest.getGroupDTO;
import static ua.com.foxminded.studenthostel.controllers.RoomControllerTest.getRoomDTO;
import static ua.com.foxminded.studenthostel.controllers.FloorControllerTest.getFloor;
import static ua.com.foxminded.studenthostel.controllers.FacultyControllerTest.getFaculty;
import static ua.com.foxminded.studenthostel.controllers.CourseNumberControllerTest.getCourseNumber;


@SpringJUnitWebConfig(WebConfig.class)
class StudentControllerTest {

    MockMvc mockMvc;

    @Autowired
    @InjectMocks
    StudentController studentController;

    @Autowired
    ExceptionController exceptionController;

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
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(studentService.getDTOById(BigInteger.ONE)).thenReturn(getStudentDTO());

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/student-info"))
                .andExpect(model().attribute("studentDTO", getStudentDTO()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(studentService.getDTOById(BigInteger.ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Student> students = Collections.singletonList(getStudent());
        Mockito.when(studentService.getAll(10, 0)).thenReturn(students);

        mockMvc.perform(get("/students/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-list"))
                .andExpect(model().attribute("students", students));
    }

    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(studentService.getAll(10, 0)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAllByFloor_ShouldReturnViewListOfFloors_WhenFloorsExists() throws Exception {
        List<Floor> floors = Collections.singletonList(getFloor());
        Mockito.when(floorService.getAll(10, 0))
                .thenReturn(floors);

        mockMvc.perform(get("/students/byFloor/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-by-floor"))
                .andExpect(model().attribute("floors", floors));
    }

    @Test
    public void getAllByFloor_ShouldReturnViewOfError_WhenFloorsNotExists() throws Exception {

        Mockito.when(floorService.getAll(10, 0))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/byFloor/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
    @Test
    public void getAllByFloorResult_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<StudentDTO> students = Collections.singletonList(getStudentDTO());
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
        Mockito.when(facultyService.getAll(10, 0))
                .thenReturn(faculties);

        mockMvc.perform(get("/students/byFaculty/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-by-faculty"))
                .andExpect(model().attribute("faculties", faculties));
    }

    @Test
    public void getAllByFaculty_ShouldReturnViewOfError_WhenFacultiesNotExists() throws Exception {

        Mockito.when(facultyService.getAll(10, 0))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/byFaculty/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
    @Test
    public void getAllByFacultyResult_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<StudentDTO> students = Collections.singletonList(getStudentDTO());
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
        Mockito.when(courseNumberService.getAll(10, 0))
                .thenReturn(courseNumbers);

        mockMvc.perform(get("/students/byCourse/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-by-course"))
                .andExpect(model().attribute("courseNumbers", courseNumbers));
    }

    @Test
    public void getAllByCourse_ShouldReturnViewOfError_WhenCourseNumbersNotExists() throws Exception {

        Mockito.when(courseNumberService.getAll(10, 0))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(get("/students/byCourse/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
    @Test
    public void getAllByCourseResult_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<StudentDTO> students = Collections.singletonList(getStudentDTO());
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

        List<StudentDTO> students = Collections.singletonList(getStudentDTO());
        Mockito.when(studentService.getAllWithDebitByGroup(getCourseNumber().getId(), 30))
                .thenReturn(students);

        mockMvc.perform(get("/students/byGroupWithDebt/group/1/debt/30"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/students-list"))
                .andExpect(model().attribute("students", students));
    }

    static Student getStudent() {
        Student student = new Student();

        student.setId(BigInteger.ONE);
        student.setFirstName("Firstname");
        student.setLastName("Lasttname");
        student.setGroupId(BigInteger.ONE);
        student.setRoomId(BigInteger.ONE);
        student.setHoursDebt(10);

        return student;
    }

    static StudentDTO getStudentDTO() {
        StudentDTO studentDTO = new StudentDTO();

        studentDTO.setId(BigInteger.ONE);
        studentDTO.setFirstName("Firstname");
        studentDTO.setLastName("Lasttname");
        studentDTO.setGroupDTO(getGroupDTO());
        studentDTO.setRoomDTO(getRoomDTO());
        studentDTO.setHoursDebt(10);
        studentDTO.setTasks(Collections.emptyList());
        studentDTO.setEquipments(Collections.emptyList());

        return studentDTO;
    }
}
