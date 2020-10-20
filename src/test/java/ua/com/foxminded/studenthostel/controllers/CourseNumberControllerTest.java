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
import ua.com.foxminded.studenthostel.service.CourseNumberService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringJUnitWebConfig(WebConfig.class)
class CourseNumberControllerTest {

    MockMvc mockMvc;

    @Autowired
    @InjectMocks
    CourseNumberController courseNumberController;
    @Autowired
    ExceptionController exceptionController;
    @Mock
    CourseNumberService courseNumberService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(courseNumberController, exceptionController)
                .build();
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(courseNumberService.getById(BigInteger.ONE)).thenReturn(getCourseNumber());

        mockMvc.perform(get("/courseNumbers/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("courseNumbers/course-info"))
                .andExpect(model().attribute("courseNumber", getCourseNumber()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(courseNumberService.getById(BigInteger.ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/courseNumbers/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<CourseNumber> courseNumbers = Collections.singletonList(getCourseNumber());
        Mockito.when(courseNumberService.getAll(10, 0)).thenReturn(courseNumbers);

        mockMvc.perform(get("/courseNumbers/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("courseNumbers/courses-list"))
                .andExpect(model().attribute("courses", courseNumbers));
    }

    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(courseNumberService.getAll(10, 0)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/courseNumbers/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    static CourseNumber getCourseNumber() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(BigInteger.ONE);
        courseNumber.setName("Testname");

        return courseNumber;
    }
}
