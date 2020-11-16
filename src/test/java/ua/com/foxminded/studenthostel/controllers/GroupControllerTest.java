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
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.service.GroupService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
class GroupControllerTest {

    private static final BigInteger ONE = BigInteger.ONE;
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private GroupController groupController;

    @Autowired
    ExceptionController exceptionController;

    @Mock
    private GroupService groupService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(groupController, exceptionController)
                .build();
    }

    @Test
    public void insert_GET_ShouldReturnInsertFormView_WhenConditionComplete() throws Exception {
        mockMvc.perform(get("/groups/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/group-insert"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfMessage_WhenEntryInserted() throws Exception {
        Mockito.when(groupService.insert(getNullIdGroup())).thenReturn(ONE);

        mockMvc.perform(post("/groups/insert")
                .param("name", "Testname")
                .param("faculty.id", ONE.toString())
                .param("courseNumber.id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Adding completed."))
                .andExpect(model().attribute("id", "New ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfError_WhenEntryNotInserted() throws Exception {
        Mockito.when(groupService.insert(getNullIdGroup())).thenThrow(DaoException.class);

        mockMvc.perform(post("/groups/insert")
                .param("name", "Testname")
                .param("faculty.id", ONE.toString())
                .param("courseNumber.id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(groupService.getById(BigInteger.ONE)).thenReturn(getGroupFull());

        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/group-info"))
                .andExpect(model().attribute("group", getGroupFull()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(groupService.getById(BigInteger.ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Group> groups = Collections.singletonList(getGroup());
        Mockito.when(groupService.getAll(0, 10)).thenReturn(groups);

        mockMvc.perform(get("/groups/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/groups-list"))
                .andExpect(model().attribute("groups", groups));
    }

    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(groupService.getAll(0, 10)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/groups/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_GET_ShouldReturnUpdateFormView_WhenConditionComplete() throws Exception {
        Mockito.when(groupService.getById(ONE)).thenReturn(getGroup());

        mockMvc.perform(get("/groups/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("groups/group-update"))
                .andExpect(model().attribute("group", getGroup()));
    }

    @Test
    public void update_GET_ShouldReturnErrorView_WhenEntryNotExist() throws Exception {
        Mockito.when(groupService.getById(ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/groups/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfMessage_WhenEntryUpdated() throws Exception {
        Mockito.when(groupService.update(getGroup())).thenReturn(getGroup());

        mockMvc.perform(post("/groups/update/1")
                .param("name", "Testname")
                .param("faculty.id", ONE.toString())
                .param("courseNumber.id", ONE.toString())
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Updating complete"))
                .andExpect(model().attribute("id", "Updated ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfError_WhenEntryNotUpdated() throws Exception {
        Mockito.when(groupService.update(getGroup())).thenThrow(DaoException.class);

        mockMvc.perform(post("/groups/update/1")
                .param("name", "Testname")
                .param("faculty.id", ONE.toString())
                .param("courseNumber.id", ONE.toString())
                .param("id", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void delete_ShouldReturnViewOfMessage_WhenEntryDeleted() throws Exception {
        Mockito.doNothing().when(groupService).deleteById(ONE);

        mockMvc.perform(post("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Deleting complete"))
                .andExpect(model().attribute("id", "Deleted group id = " + ONE))
                .andExpect(view().name("message"));
    }

    static Group getNullIdGroup() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(ONE);

        Faculty faculty = new Faculty();
        faculty.setId(ONE);

        Group group = new Group();
        group.setCourseNumber(courseNumber);
        group.setFaculty(faculty);
        group.setName("Testname");

        return group;
    }

    static Group getGroup() {

        Group group = getNullIdGroup();
        group.setId(BigInteger.ONE);
        return group;
    }

    static Group getGroupFull() {
        Group group = getGroup();

        group.setId(group.getId());
        group.setName(group.getName());
        group.setFaculty(FacultyControllerTest.getFaculty());
        group.setCourseNumber(CourseNumberControllerTest.getCourseNumber());

        return group;
    }
}
