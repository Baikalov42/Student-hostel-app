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
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.service.TaskService;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringJUnitWebConfig(WebConfig.class)
class TaskControllerTest {

    MockMvc mockMvc;

    @Autowired
    @InjectMocks
    TaskController taskController;

    @Autowired
    ExceptionController exceptionController;

    @Mock
    TaskService taskService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(taskController, exceptionController)
                .build();
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(taskService.getById(BigInteger.ONE)).thenReturn(getTask());

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/task-info"))
                .andExpect(model().attribute("task", getTask()));
    }

    @Test
    public void getById_ShouldReturnViewOfError_WhenIdNotExist() throws Exception {
        Mockito.when(taskService.getById(BigInteger.ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getAll_ShouldReturnViewWithResultList_WhenEntriesExists() throws Exception {
        List<Task> floors = Collections.singletonList(getTask());
        Mockito.when(taskService.getAll(10, 0)).thenReturn(floors);

        mockMvc.perform(get("/tasks/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/tasks-list"))
                .andExpect(model().attribute("tasks", floors));
    }
    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(taskService.getAll(10, 0)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/tasks/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    static Task getTask() {
        Task task = new Task();
        task.setDescription("Test Description");
        task.setId(BigInteger.ONE);
        task.setName("Testname");

        return task;
    }
}
