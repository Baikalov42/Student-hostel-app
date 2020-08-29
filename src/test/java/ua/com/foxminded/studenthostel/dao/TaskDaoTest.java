package ua.com.foxminded.studenthostel.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.Task;

import javax.sql.DataSource;
import java.math.BigInteger;

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

        Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> taskDao.getById(BigInteger.valueOf(10)));
    }

    @Test
    public void setToStudent_ShouldInsertNewEntry_ToStudentsTasksTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");
        taskDao.setToStudent(BigInteger.valueOf(1), BigInteger.valueOf(1));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_tasks");

        Assertions.assertEquals(rowBefore + 1, rowAfter);
    }


    @Test
    public void setToStudent_ShouldThrowException_WhenStudentIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> taskDao.setToStudent(BigInteger.valueOf(1), BigInteger.valueOf(3)));
    }

    @Test
    public void setToStudent_ShouldThrowException_WhenTaskIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> taskDao.setToStudent(BigInteger.valueOf(2), BigInteger.valueOf(1)));
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
    public void deleteById_ShouldDeleteEntry_FromTasksTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToTasksTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");
        taskDao.deleteById(BigInteger.valueOf(4));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "tasks");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }
}
