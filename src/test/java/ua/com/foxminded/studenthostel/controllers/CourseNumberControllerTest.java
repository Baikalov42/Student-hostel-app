package ua.com.foxminded.studenthostel.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.studenthostel.controllers.handlers.ExceptionController;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.service.CourseNumberService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class CourseNumberControllerTest {

    private static final BigInteger ONE = BigInteger.ONE;
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private CourseNumberController courseNumberController;

    @Autowired
    private ExceptionController exceptionController;

    @Mock
    private CourseNumberService courseNumberService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(courseNumberController, exceptionController)
                .build();
    }

    @Test
    public void insert_GET_ShouldReturnInsertFormView_WhenConditionComplete() throws Exception {
        mockMvc.perform(get("/courseNumbers/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("courseNumbers/course-insert"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfMessage_WhenEntryInserted() throws Exception {
        Mockito.when(courseNumberService.insert(getCourseNumberNullId())).thenReturn(ONE);

        mockMvc.perform(post("/courseNumbers/insert")
                .param("name", "Testname"))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Adding completed."))
                .andExpect(model().attribute("id", "New ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfError_WhenEntryNotInserted() throws Exception {
        Mockito.when(courseNumberService.insert(getCourseNumberNullId())).thenThrow(DaoException.class);

        mockMvc.perform(post("/courseNumbers/insert")
                .param("name", "Testname"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(courseNumberService.getById(ONE)).thenReturn(getCourseNumber());

        mockMvc.perform(get("/courseNumbers/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("courseNumbers/course-info"))
                .andExpect(model().attribute("courseNumber", getCourseNumber()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(courseNumberService.getById(ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/courseNumbers/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<CourseNumber> courseNumbers = Collections.singletonList(getCourseNumber());
        Mockito.when(courseNumberService.getAll(0)).thenReturn(courseNumbers);

        mockMvc.perform(get("/courseNumbers/page/0"))
                .andExpect(status().isOk())
                .andExpect(view().name("courseNumbers/courses-list"))
                .andExpect(model().attribute("courses", courseNumbers));
    }

    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(courseNumberService.getAll(0)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/courseNumbers/page/0"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_GET_ShouldReturnUpdateFormView_WhenConditionComplete() throws Exception {
        Mockito.when(courseNumberService.getById(ONE)).thenReturn(getCourseNumber());

        mockMvc.perform(get("/courseNumbers/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("courseNumbers/course-update"))
                .andExpect(model().attribute("courseNumber", getCourseNumber()));
    }

    @Test
    public void update_GET_ShouldReturnErrorView_WhenEntryNotExist() throws Exception {
        Mockito.when(courseNumberService.getById(ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/courseNumbers/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfMessage_WhenEntryUpdated() throws Exception {
        Mockito.when(courseNumberService.update(getCourseNumber())).thenReturn(getCourseNumber());

        mockMvc.perform(post("/courseNumbers/update/1")
                .param("name", "Testname")
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Updating complete"))
                .andExpect(model().attribute("id", "Updated ID = " + getCourseNumber().getId()))
                .andExpect(view().name("message"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfError_WhenEntryNotUpdated() throws Exception {
        Mockito.when(courseNumberService.update(getCourseNumber())).thenThrow(DaoException.class);

        mockMvc.perform(post("/courseNumbers/update/1")
                .param("name", "Testname")
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void delete_ShouldReturnViewOfMessage_WhenEntryDeleted() throws Exception {
        Mockito.doNothing().when(courseNumberService).deleteById(ONE);

        mockMvc.perform(post("/courseNumbers/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Deleting complete"))
                .andExpect(model().attribute("id", "Deleted course id = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void delete_ShouldReturnViewOfError_WhenEntryNotDeleted() throws Exception {
        Mockito.doThrow(DaoException.class).when(courseNumberService).deleteById(ONE);

        mockMvc.perform(post("/courseNumbers/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    static CourseNumber getCourseNumber() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(BigInteger.ONE);
        courseNumber.setName("Testname");

        return courseNumber;
    }

    static CourseNumber getCourseNumberNullId() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("Testname");

        return courseNumber;
    }
}
