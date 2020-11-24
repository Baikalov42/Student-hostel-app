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
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.service.FacultyService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class FacultyControllerTest {

    private static final BigInteger ONE = BigInteger.ONE;
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private FacultyController facultyController;

    @Autowired
    private ExceptionController exceptionController;

    @Mock
    private FacultyService facultyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(facultyController, exceptionController)
                .build();
    }

    @Test
    public void insert_GET_ShouldReturnInsertFormView_WhenConditionComplete() throws Exception {
        mockMvc.perform(get("/faculties/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("faculties/faculty-insert"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfMessage_WhenEntryInserted() throws Exception {
        Mockito.when(facultyService.insert(getNullIdFaculty())).thenReturn(ONE);

        mockMvc.perform(post("/faculties/insert")
                .param("name", "Testname"))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Adding completed."))
                .andExpect(model().attribute("id", "New ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfError_WhenEntryNotInserted() throws Exception {
        Mockito.when(facultyService.insert(getNullIdFaculty())).thenThrow(DaoException.class);

        mockMvc.perform(post("/faculties/insert")
                .param("name", "Testname"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
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
        Mockito.when(facultyService.getAll(0)).thenReturn(faculties);

        mockMvc.perform(get("/faculties/page/0"))
                .andExpect(status().isOk())
                .andExpect(view().name("faculties/faculties-list"))
                .andExpect(model().attribute("faculties", faculties));
    }

    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(facultyService.getAll(0)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/faculties/page/0"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_GET_ShouldReturnUpdateFormView_WhenConditionComplete() throws Exception {
        Mockito.when(facultyService.getById(ONE)).thenReturn(getFaculty());

        mockMvc.perform(get("/faculties/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("faculties/faculty-update"))
                .andExpect(model().attribute("faculty", getFaculty()));
    }

    @Test
    public void update_GET_ShouldReturnErrorView_WhenEntryNotExist() throws Exception {
        Mockito.when(facultyService.getById(ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/faculties/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfMessage_WhenEntryUpdated() throws Exception {
        Mockito.when(facultyService.update(getFaculty())).thenReturn(getFaculty());

        mockMvc.perform(post("/faculties/update/1")
                .param("name", "Testname")
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Updating complete"))
                .andExpect(model().attribute("id", "Updated ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfError_WhenEntryNotUpdated() throws Exception {
        Mockito.when(facultyService.update(getFaculty())).thenThrow(DaoException.class);

        mockMvc.perform(post("/faculties/update/1")
                .param("name", "Testname")
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void delete_ShouldReturnViewOfMessage_WhenEntryDeleted() throws Exception {
        Mockito.doNothing().when(facultyService).deleteById(ONE);

        mockMvc.perform(post("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Deleting complete"))
                .andExpect(model().attribute("id", "Deleted faculty id = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void delete_ShouldReturnViewOfError_WhenEntryNotDeleted() throws Exception {
        doThrow(DaoException.class).when(facultyService).deleteById(ONE);

        mockMvc.perform(post("/faculties/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    static Faculty getFaculty() {
        Faculty faculty = getNullIdFaculty();
        faculty.setId(ONE);

        return faculty;
    }

    static Faculty getNullIdFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Testname");

        return faculty;
    }
}
