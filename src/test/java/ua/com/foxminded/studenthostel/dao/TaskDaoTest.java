package ua.com.foxminded.studenthostel.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Task;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class TaskDaoTest {

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
        sqlScripts.addScript(new ClassPathResource("sql\\CreateTables.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);
    }

    @Test
    public void insert_ShouldMakeEntry_InTasksTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Task task = new Task();
        task.setId(BigInteger.valueOf(2));
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

        task.setId(BigInteger.valueOf(1));
        task.setName("test");
        task.setDescription("test");
        task.setCostInHours(1);

        Assertions.assertEquals(task, taskDao.getById(BigInteger.valueOf(1)));
    }


    @Test
    public void getById_ShouldThrowException_WhenTaskIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(DaoException.class,
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
        taskDao.assignToStudent(BigInteger.valueOf(1), BigInteger.valueOf(1));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");

        Assertions.assertEquals(rowBefore + 1, rowAfter);
    }


    @Test
    public void assignToStudent_ShouldThrowException_WhenStudentIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> taskDao.assignToStudent(BigInteger.valueOf(3), BigInteger.valueOf(1)));
    }

    @Test
    public void assignToStudent_ShouldThrowException_WhenTaskIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> taskDao.assignToStudent(BigInteger.valueOf(1), BigInteger.valueOf(2)));
    }


    @Test
    public void removeFromStudent_ShouldDeleteEntry_FromStudentsTasksTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");
        taskDao.removeFromStudent(BigInteger.valueOf(1), BigInteger.valueOf(4));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }

    @Test
    public void isStudentTaskRelationExist_ShouldReturnTrue_WhenEntryExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertTrue(taskDao.isStudentTaskRelationExist(BigInteger.valueOf(1), BigInteger.valueOf(4)));
    }

    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Task newValues = new Task();
        newValues.setId(BigInteger.valueOf(1));
        newValues.setName("newname");
        newValues.setDescription("newdescription");
        newValues.setCostInHours(1);

        boolean isUpdated = taskDao.update(newValues);

        Assertions.assertTrue(isUpdated);
        Assertions.assertEquals(newValues, taskDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void update_ShouldReturnFalse_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Task newValues = new Task();
        newValues.setId(BigInteger.valueOf(3));
        newValues.setName("newname");
        newValues.setDescription("newdescription");
        newValues.setCostInHours(1);

        Assertions.assertFalse(taskDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldReturnTrue_WhenEntryIsDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");
        boolean isDeleted = taskDao.deleteById(BigInteger.valueOf(4));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
        Assertions.assertTrue(isDeleted);
    }

    @Test
    public void deleteById_ShouldReturnFalse_WhenEntryNotDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");
        boolean isDeleted = taskDao.deleteById(BigInteger.valueOf(5));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");

        Assertions.assertEquals(rowBefore, rowAfter);
        Assertions.assertFalse(isDeleted);
    }
}
