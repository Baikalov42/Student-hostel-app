package ua.com.foxminded.studenthostel.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TaskRepositoryTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql("/sql/AddDataToTasksTable.sql")
    public void insert_ShouldMakeEntry_InTasksTable() {

        Task task = new Task();
        task.setName("Testtaskname");
        task.setDescription("Testdescription");
        task.setCostInHours(10);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");
        taskRepository.saveAndFlush(task);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");

        Assertions.assertEquals(rowBefore + 1, rowAfter);
    }

    @Test
    @Sql("/sql/AddDataToTasksTable.sql")
    public void getById_ShouldReturnTask_WhenTaskIdIsExist() {

        Task task = new Task();

        task.setId(ONE);
        task.setName("Test");
        task.setDescription("Test");
        task.setCostInHours(1);

        Assertions.assertEquals(task, taskRepository.findById(ONE).get());
    }

    @Test
    @Sql("/sql/AddDataToTasksTable.sql")
    public void getAll_ShouldReturnListOfTasks_WhenConditionCompleted() {

        Task task = new Task();
        task.setId(BigInteger.valueOf(4));
        task.setName("Testtwo");
        task.setDescription("Testtwo");
        task.setCostInHours(4);

        List<Task> list = new ArrayList<>();
        list.add(task);

        Pageable pageable = PageRequest.of(1, 1, Sort.Direction.ASC, "id");
        Assertions.assertEquals(list, taskRepository.findAll(pageable).getContent());
    }

    @Test
    @Sql("/sql/AddDataToTasksTable.sql")
    public void assignToStudent_ShouldInsertNewEntry_ToStudentsTasksTable() {

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");
        taskRepository.assignToStudent(ONE, ONE);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");

        Assertions.assertEquals(rowBefore + 1, rowAfter);
    }


    @Test
    @Sql("/sql/AddDataToTasksTable.sql")
    public void assignToStudent_ShouldThrowException_WhenStudentIdNotExist() {

        Assertions.assertThrows(NotFoundException.class,
                () -> taskRepository.assignToStudent(BigInteger.valueOf(3), ONE));
    }

    @Test
    @Sql("/sql/AddDataToTasksTable.sql")
    public void assignToStudent_ShouldThrowException_WhenTaskIdNotExist() {

        Assertions.assertThrows(NotFoundException.class,
                () -> taskRepository.assignToStudent(ONE, BigInteger.valueOf(2)));
    }


    @Test
    @Sql("/sql/AddDataToTasksTable.sql")
    public void removeFromStudent_ShouldDeleteEntry_FromStudentsTasksTable() {

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");
        taskRepository.unassignFromStudent(ONE, BigInteger.valueOf(4));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }

    @Test
    @Sql("/sql/AddDataToTasksTable.sql")
    public void isStudentTaskRelationExist_ShouldReturnTrue_WhenEntryExist() {
        Assertions.assertTrue(taskRepository.isStudentTaskRelationExist(ONE, BigInteger.valueOf(4)));
    }

    @Test
    @Sql("/sql/AddDataToTasksTable.sql")
    public void update_ShouldUpdateEntry_WhenDataExist() {

        Task newValues = new Task();
        newValues.setId(ONE);
        newValues.setName("Newname");
        newValues.setDescription("Newdescription");
        newValues.setCostInHours(1);

        taskRepository.saveAndFlush(newValues);

        Assertions.assertEquals(newValues, taskRepository.findById(ONE).get());
    }

    @Test
    @Sql("/sql/AddDataToTasksTable.sql")
    public void update_ShouldThrowException_WhenDataNotExist() {

        Task newValues = new Task();
        newValues.setId(BigInteger.valueOf(3));
        newValues.setName("Newname");
        newValues.setDescription("Newdescription");
        newValues.setCostInHours(1);

        Assertions.assertThrows(DataAccessException.class,
                () -> taskRepository.saveAndFlush(newValues));
    }

    @Test
    @Sql("/sql/AddDataToTasksTable.sql")
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");
        taskRepository.deleteById(ONE);
        taskRepository.flush();
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }

}
