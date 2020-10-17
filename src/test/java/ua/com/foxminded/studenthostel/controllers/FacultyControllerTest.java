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
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.service.FacultyService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringJUnitWebConfig(WebConfig.class)
class FacultyControllerTest {

    MockMvc mockMvc;

    @Autowired
    @InjectMocks
    FacultyController facultyController;
    @Autowired
    ExceptionController exceptionController;
    @Mock
    FacultyService facultyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(facultyController, exceptionController)
                .build();
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(facultyService.getById(BigInteger.ONE)).thenReturn(getFaculty());

        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("faculties/faculty-info"))
                .andExpect(model().attribute("faculty", getFaculty()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(facultyService.getById(BigInteger.ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Faculty> faculties = Collections.singletonList(getFaculty());
        Mockito.when(facultyService.getAll(10, 0)).thenReturn(faculties);

        mockMvc.perform(get("/faculties/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("faculties/faculties-list"))
                .andExpect(model().attribute("faculties", faculties));
    }
    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(facultyService.getAll(10, 0)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/faculties/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    static Faculty getFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(BigInteger.ONE);
        faculty.setName("Testname");

        return faculty;
    }
}
