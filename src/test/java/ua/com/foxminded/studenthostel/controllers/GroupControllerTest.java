package ua.com.foxminded.studenthostel.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.dto.GroupDTO;
import ua.com.foxminded.studenthostel.service.GroupService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringJUnitWebConfig(WebConfig.class)
class GroupControllerTest {

    MockMvc mockMvc;

    @Autowired
    @InjectMocks
    GroupController groupController;
    @Autowired
    ExceptionController exceptionController;
    @Mock
    GroupService groupService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(groupController, exceptionController)
                .build();
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(groupService.getDTOById(BigInteger.ONE)).thenReturn(getGroupDTO());

        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/group-info"))
                .andExpect(model().attribute("groupDTO", getGroupDTO()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(groupService.getDTOById(BigInteger.ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Group> groups = Collections.singletonList(getGroup());
        Mockito.when(groupService.getAll(10, 0)).thenReturn(groups);

        mockMvc.perform(get("/groups/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/groups-list"))
                .andExpect(model().attribute("groups", groups));
    }

    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(groupService.getAll(10, 0)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/groups/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    static Group getGroup() {
        Group group = new Group();
        group.setCourseNumberId(BigInteger.ONE);
        group.setCourseNumberId(BigInteger.ONE);
        group.setId(BigInteger.ONE);
        group.setName("Testname");

        return group;
    }

    static GroupDTO getGroupDTO() {

        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(BigInteger.ONE);
        courseNumber.setName("Testname");

        GroupDTO groupDTO = new GroupDTO();

        groupDTO.setId(getGroup().getId());
        groupDTO.setName(getGroup().getName());
        groupDTO.setFaculty(FacultyControllerTest.getFaculty());
        groupDTO.setCourseNumber(courseNumber);

        return groupDTO;
    }
}
