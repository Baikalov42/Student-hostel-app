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
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.service.StudentService;
import ua.com.foxminded.studenthostel.service.TaskService;

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
class TaskControllerTest {

    private static final BigInteger ONE = BigInteger.ONE;
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private TaskController taskController;

    @Autowired
    private ExceptionController exceptionController;

    @Mock
    private TaskService taskService;

    @Mock
    private StudentService studentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(taskController, exceptionController)
                .build();
    }

    @Test
    public void insert_GET_ShouldReturnInsertFormView_WhenConditionComplete() throws Exception {
        mockMvc.perform(get("/tasks/insert"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/task-insert"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfMessage_WhenEntryInserted() throws Exception {
        Mockito.when(taskService.insert(getNullIdTask())).thenReturn(ONE);

        mockMvc.perform(post("/tasks/insert")
                .param("name", "Testname")
                .param("cost", ONE.toString())
                .param("description", "Test Description"))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Adding completed."))
                .andExpect(model().attribute("id", "New ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void insert_POST_ShouldReturnViewOfError_WhenEntryNotInserted() throws Exception {
        Mockito.when(taskService.insert(getNullIdTask())).thenThrow(DaoException.class);

        mockMvc.perform(post("/tasks/insert")
                .param("name", "Testname")
                .param("cost", ONE.toString())
                .param("description", "Test Description"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void getById_ShouldReturnViewOfEntry_WhenIdIsExist() throws Exception {
        Mockito.when(taskService.getById(ONE)).thenReturn(getTask());

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
        Mockito.when(taskService.getAll(0, 10)).thenReturn(floors);

        mockMvc.perform(get("/tasks/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/tasks-list"))
                .andExpect(model().attribute("tasks", floors));
    }

    @Test
    public void getAll_ShouldReturnViewOfError_WhenResultIsEmpty() throws Exception {
        Mockito.when(taskService.getAll(0, 10)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/tasks/page/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void assignToStudent_GET_ShouldReturnFormView_WhenConditionComplete() throws Exception {
        Mockito.when(studentService.getById(ONE)).thenReturn(StudentControllerTest.getStudent());

        mockMvc.perform(get("/tasks/assign/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student", StudentControllerTest.getStudent()))
                .andExpect(view().name("tasks/task-assign-to-student"));
    }

    @Test
    public void assignToStudent_POST_ShouldReturnMessageView_WhenConditionComplete() throws Exception {
        Mockito.doNothing().when(taskService).assignToStudent(ONE, ONE);

        mockMvc.perform(post("/tasks/assign")
                .param("studentId", ONE.toString())
                .param("secondId", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Assigning success"))
                .andExpect(model().attribute("id", "Student ID = " + ONE + ", Task ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void assignToStudent_POST_ShouldReturnErrorView_WhenTaskNotAssigned() throws Exception {
        Mockito.doThrow(DaoException.class).when(taskService).assignToStudent(ONE, ONE);

        mockMvc.perform(post("/tasks/assign")
                .param("studentId", ONE.toString())
                .param("secondId", ONE.toString()))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void unassignFromStudent_ShouldReturnFormView_WhenConditionComplete() throws Exception {
        Mockito.doNothing().when(taskService).unassignFromStudent(ONE, ONE);

        mockMvc.perform(post("/tasks/unassign/1/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Un assigning success!"))
                .andExpect(model().attribute("id", "Student ID = " + ONE + ", Task ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void unassignFromStudent_ShouldReturnErrorView_WhenTaskNotUnassigned() throws Exception {

        doThrow(DaoException.class).when(taskService).unassignFromStudent(ONE, ONE);

        mockMvc.perform(post("/tasks/unassign/1/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void acceptTaskAndUpdateHours_ShouldReturnMessageView_WhenConditionComplete() throws Exception {
        Mockito.when(studentService.acceptTaskAndUpdateHours(ONE, ONE)).thenReturn(10);

        mockMvc.perform(post("/tasks/accept/1/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Un assigning success!"))
                .andExpect(model().attribute("id", "For student ID = " + ONE + " new hours count :" + 10))
                .andExpect(view().name("message"));
    }


    @Test
    public void update_GET_ShouldReturnUpdateFormView_WhenConditionComplete() throws Exception {
        Mockito.when(taskService.getById(ONE)).thenReturn(getTask());

        mockMvc.perform(get("/tasks/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("tasks/task-update"))
                .andExpect(model().attribute("task", getTask()));
    }

    @Test
    public void update_GET_ShouldReturnErrorView_WhenEntryNotExist() throws Exception {
        Mockito.when(taskService.getById(ONE)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/tasks/update/1"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfMessage_WhenEntryUpdated() throws Exception {
        Mockito.when(taskService.update(getTask())).thenReturn(getTask());

        mockMvc.perform(post("/tasks/update/1")
                .param("id", ONE.toString())
                .param("name", "Testname")
                .param("cost", ONE.toString())
                .param("description", "Test Description"))

                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Updating complete"))
                .andExpect(model().attribute("id", "Updated ID = " + ONE))
                .andExpect(view().name("message"));
    }

    @Test
    public void update_POST_ShouldReturnViewOfError_WhenEntryNotUpdated() throws Exception {
        Mockito.when(taskService.update(getTask())).thenThrow(DaoException.class);

        mockMvc.perform(post("/tasks/update/1")
                .param("id", ONE.toString())
                .param("name", "Testname")
                .param("cost", ONE.toString())
                .param("description", "Test Description"))

                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    public void delete_ShouldReturnViewOfMessage_WhenEntryDeleted() throws Exception {
        Mockito.doNothing().when(taskService).deleteById(ONE);

        mockMvc.perform(post("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("message", "Deleting complete"))
                .andExpect(model().attribute("id", "Deleted task id = " + ONE))
                .andExpect(view().name("message"));
    }

    static Task getNullIdTask() {
        Task task = new Task();
        task.setDescription("Test Description");
        task.setName("Testname");

        return task;
    }

    static Task getTask() {
        Task task = getNullIdTask();
        task.setId(BigInteger.ONE);

        return task;
    }
}
