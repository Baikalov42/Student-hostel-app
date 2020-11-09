package ua.com.foxminded.studenthostel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.dao.StudentDao;
import ua.com.foxminded.studenthostel.dao.TaskDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.Task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class TaskServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;

    public static final String VALID_NAME = "Valid name";
    public static final String VALID_DESC = "Description description 123";

    public static final int VALID_COST = 6;

    @Autowired
    @InjectMocks
    private TaskService taskService;

    @Autowired
    @InjectMocks
    private StudentService studentService;

    @Mock
    private TaskDao taskDao;
    @Mock
    private StudentDao studentDao;


    @BeforeEach
    public void initMock() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Task task = new Task();
        task.setName(VALID_NAME);
        task.setDescription(VALID_DESC);
        task.setCostInHours(VALID_COST);

        Mockito.when(taskDao.insert(task)).thenReturn(VALID_ID);

        Assertions.assertEquals(VALID_ID, taskService.insert(task));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenNameNotValid() {
        Task task = new Task();
        task.setDescription(VALID_DESC);
        task.setCostInHours(VALID_COST);

        task.setName("");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName("1");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName("A");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName("a");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName("ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName("AB-123");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName("Ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName("AB_1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName("AB-12345");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenDescriptionNotValid() {
        Task task = new Task();
        task.setName(VALID_NAME);
        task.setCostInHours(VALID_COST);

        task.setDescription("");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription(" ");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription(" ");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription("1");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription("123");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription("A");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription("a");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription("ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription("AB-123");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription("Ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription("AB_1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription("AB-12345");
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setDescription(null);
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenCostInHoursNotValid() {
        Task task = new Task();
        task.setDescription(VALID_DESC);
        task.setName(VALID_NAME);

        task.setCostInHours(0);
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setCostInHours(-1);
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

        task.setCostInHours(11);
        Assertions.assertThrows(ValidationException.class, () -> taskService.insert(task));

    }

    @Test
    public void getById_ShouldReturnObject_WhenIdIsValid() {
        Task task = new Task();
        task.setDescription(VALID_DESC);
        task.setName(VALID_NAME);
        task.setCostInHours(VALID_COST);

        Mockito.when(taskDao.getById(VALID_ID)).thenReturn(task);
        Assertions.assertEquals(task, taskService.getById(VALID_ID));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> taskService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> taskService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> taskService.getById(null));

    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(taskDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> taskService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldReturnResultList_WhenConditionCompleted() {
        Task task = new Task();
        task.setId(VALID_ID);
        task.setDescription(VALID_DESC);
        task.setName(VALID_NAME);
        task.setCostInHours(VALID_COST);

        List<Task> expectResult = new ArrayList<>();
        expectResult.add(task);

        Mockito.when(taskDao.getAll(1, 10)).thenReturn(expectResult);
        Assertions.assertEquals(expectResult, taskService.getAll(1, 10));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(taskDao.getAll(10, 10)).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NotFoundException.class,
                () -> taskService.getAll(10, 10));
    }

    @Test
    public void assignToStudent_ShouldThrowException_WhenIdNotValid() {


        Assertions.assertThrows(ValidationException.class,
                () -> taskService.assignToStudent(ZERO_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.assignToStudent(NEGATIVE_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.assignToStudent(VALID_ID, ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.assignToStudent(VALID_ID, NEGATIVE_ID));
    }

    @Test
    public void assignToStudent_ShouldThrowException_WhenStudentNotExist() {
        Mockito.when(studentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Mockito.when(taskDao.getById(VALID_ID)).thenReturn(new Task());

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.assignToStudent(BigInteger.ONE, BigInteger.ONE));
    }

    @Test
    public void assignToStudent_ShouldThrowException_WhenTaskNotExist() {
        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(taskDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.assignToStudent(BigInteger.ONE, BigInteger.ONE));
    }


    @Test
    public void unassignFromStudent_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.unassignFromStudent(ZERO_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.unassignFromStudent(NEGATIVE_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.unassignFromStudent(VALID_ID, ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.unassignFromStudent(VALID_ID, NEGATIVE_ID));
    }


    @Test
    public void unassignFromStudent_ShouldThrowException_WhenStudentNotExist() {
        Mockito.when(studentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Mockito.when(taskDao.getById(VALID_ID)).thenReturn(new Task());

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.unassignFromStudent(BigInteger.ONE, BigInteger.ONE));
    }

    @Test
    public void unassignFromStudent_ShouldThrowException_WhenTaskNotExist() {
        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(taskDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.unassignFromStudent(BigInteger.ONE, BigInteger.ONE));
    }

    @Test
    public void isStudentTaskRelationExist_ShouldReturnTrue_WhenEntryIsExist() {

        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(taskDao.getById(VALID_ID)).thenReturn(new Task());

        Mockito.when(taskDao.isStudentTaskRelationExist(VALID_ID, VALID_ID)).thenReturn(true);

        Assertions.assertTrue(taskService.isStudentTaskRelationExist(VALID_ID, VALID_ID));
    }

    @Test
    public void isStudentTaskRelationExist_ShouldReturnFalse_WhenEntryNotExist() {

        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(taskDao.getById(VALID_ID)).thenReturn(new Task());

        Mockito.when(taskDao.isStudentTaskRelationExist(VALID_ID, VALID_ID)).thenReturn(false);

        Assertions.assertFalse(taskService.isStudentTaskRelationExist(VALID_ID, VALID_ID));
    }

    @Test
    public void isStudentTaskRelationExist_ShouldThrowException_WhenStudentNotExist() {

        Mockito.when(studentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Mockito.when(taskDao.getById(VALID_ID)).thenReturn(new Task());

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.isStudentTaskRelationExist(VALID_ID, VALID_ID));
    }

    @Test
    public void isStudentTaskRelationExist_ShouldThrowException_WhenTaskNotExist() {

        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(taskDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.isStudentTaskRelationExist(VALID_ID, VALID_ID));
    }

    @Test
    public void isStudentTaskRelationExist_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.isStudentTaskRelationExist(ZERO_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.isStudentTaskRelationExist(NEGATIVE_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.isStudentTaskRelationExist(VALID_ID, ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.isStudentTaskRelationExist(VALID_ID, NEGATIVE_ID));
    }


    @Test
    public void update_ShouldThrowException_WhenNameNotValid() {
        Task task = new Task();

        task.setId(VALID_ID);
        task.setDescription(VALID_DESC);
        task.setCostInHours(VALID_COST);

        task.setName("");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName("1");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName("A");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName("a");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName("ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName("AB-123");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName("Ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName("AB_1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName("AB-12345");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));
    }

    @Test
    public void update_ShouldThrowException_WhenDescriptionNotValid() {
        Task task = new Task();
        task.setId(VALID_ID);
        task.setName(VALID_NAME);
        task.setCostInHours(VALID_COST);

        task.setDescription("");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription(" ");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription(" ");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription("1");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription("123");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription("A");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription("a");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription("ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription("AB-123");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription("Ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription("AB_1234");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription("AB-12345");
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setDescription(null);
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

    }

    @Test
    public void update_ShouldThrowException_WhenCostNotValid() {
        Task task = new Task();

        task.setId(VALID_ID);
        task.setName(VALID_NAME);
        task.setDescription(VALID_DESC);

        task.setCostInHours(0);
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setCostInHours(-1);
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setCostInHours(11);
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));
    }

    @Test
    public void update_ShouldThrowException_WhenIdNotValid() {
        Task task = new Task();
        task.setName(VALID_NAME);
        task.setDescription(VALID_DESC);
        task.setCostInHours(VALID_COST);

        task.setId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));

        task.setId(null);
        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));
    }

    @Test
    public void update_ShouldThrowException_WhenEntryNotExist() {
        Task task = new Task();
        task.setName(VALID_NAME);
        task.setDescription(VALID_DESC);
        task.setCostInHours(VALID_COST);

        Mockito.when(taskDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> taskService.update(task));
    }

    @Test
    public void update_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> taskService.update(null));
    }

    @Test
    public void deleteById_ShouldThrowException_WhenIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> taskService.deleteById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.deleteById(BigInteger.ZERO));

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.deleteById(null));
    }

    @Test
    public void deleteById_ShouldThrowException_WhenEntryNotExist() {
        Mockito.when(taskDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> taskService.deleteById(VALID_ID));
    }
}
