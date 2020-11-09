package ua.com.foxminded.studenthostel.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Task;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringJUnitConfig(SpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TaskDaoTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private DataSource dataSource;

    @Autowired
    TaskDao taskDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static ResourceDatabasePopulator sqlScripts;

    @BeforeEach
    public void addTablesScript() {
        sqlScripts = new ResourceDatabasePopulator();
    }

    @Test
    public void insert_ShouldMakeEntry_InTasksTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Task task = new Task();
        task.setName("testtaskname");
        task.setDescription("testdescription");
        task.setCostInHours(10);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");
        taskDao.insert(task);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");

        Assertions.assertEquals(rowBefore + 1, rowAfter);
    }

    @Test
    public void getById_ShouldReturnTask_WhenTaskIdIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Task task = new Task();

        task.setId(ONE);
        task.setName("test");
        task.setDescription("test");
        task.setCostInHours(1);

        Assertions.assertEquals(task, taskDao.getById(ONE));
    }


    @Test
    public void getById_ShouldThrowException_WhenTaskIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(NotFoundException.class,
                () -> taskDao.getById(BigInteger.valueOf(10)));
    }

    @Test
    public void getAll_ShouldReturnListOfTasks_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Task task = new Task();
        task.setId(BigInteger.valueOf(4));
        task.setName("testtwo");
        task.setDescription("testtwo");
        task.setCostInHours(4);

        List<Task> list = new ArrayList<>();
        list.add(task);

        Assertions.assertEquals(list, taskDao.getAll(1, 1));
    }

    @Test
    public void assignToStudent_ShouldInsertNewEntry_ToStudentsTasksTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");
        taskDao.assignToStudent(ONE, ONE);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");

        Assertions.assertEquals(rowBefore + 1, rowAfter);
    }


    @Test
    public void assignToStudent_ShouldThrowException_WhenStudentIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(NotFoundException.class,
                () -> taskDao.assignToStudent(BigInteger.valueOf(3), ONE));
    }

    @Test
    public void assignToStudent_ShouldThrowException_WhenTaskIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(NotFoundException.class,
                () -> taskDao.assignToStudent(ONE, BigInteger.valueOf(2)));
    }


    @Test
    public void removeFromStudent_ShouldDeleteEntry_FromStudentsTasksTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");
        taskDao.unassignFromStudent(ONE, BigInteger.valueOf(4));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }

    @Test
    public void isStudentTaskRelationExist_ShouldReturnTrue_WhenEntryExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertTrue(taskDao.isStudentTaskRelationExist(ONE, BigInteger.valueOf(4)));
    }

    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Task newValues = new Task();
        newValues.setId(ONE);
        newValues.setName("newname");
        newValues.setDescription("newdescription");
        newValues.setCostInHours(1);

        taskDao.update(newValues);

        Assertions.assertEquals(newValues, taskDao.getById(ONE));
    }

    @Test
    public void update_ShouldThrowException_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Task newValues = new Task();
        newValues.setId(BigInteger.valueOf(3));
        newValues.setName("newname");
        newValues.setDescription("newdescription");
        newValues.setCostInHours(1);

        Assertions.assertThrows(DaoException.class, () -> taskDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");
         taskDao.deleteById(ONE);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }

}
