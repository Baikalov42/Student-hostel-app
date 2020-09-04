package ua.com.foxminded.studenthostel.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.CourseNumber;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class CourseNumberDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    CourseNumberDao courseNumberDao;

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
    public void insert_ShouldMakeEntry_InCourseNumbersTable() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("first");

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");
        courseNumberDao.insert(courseNumber);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void insert_ShouldReturnId_WhenEntryIsInserted() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("first");
        Assertions.assertEquals(BigInteger.valueOf(1), courseNumberDao.insert(courseNumber));
    }

    @Test
    public void insert_ShouldThrowException_WhenNameNotUnique() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("first");

        Assertions.assertThrows(DuplicateKeyException.class, () -> courseNumberDao.insert(courseNumber));
    }

    @Test
    public void getById_ShouldReturnCorrectEntry_WhenIdIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(BigInteger.valueOf(1));
        courseNumber.setName("first");

        Assertions.assertEquals(courseNumber, courseNumberDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getById_ShouldThrowException_WhenEntityNotExist() {

        Assertions.assertThrows(DaoException.class,
                () -> courseNumberDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getAll_ShouldReturnListOfCourseNumbers_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        List<CourseNumber> courseNumbers = new ArrayList<>();
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(BigInteger.valueOf(4));
        courseNumber.setName("fourth");

        courseNumbers.add(courseNumber);
        Assertions.assertEquals(courseNumbers, courseNumberDao.getAll(1, 3));
    }

    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        CourseNumber newValues = new CourseNumber();
        newValues.setId(BigInteger.valueOf(1));
        newValues.setName("updated");

        boolean isUpdated = courseNumberDao.update(newValues);

        Assertions.assertTrue(isUpdated);
        Assertions.assertEquals(newValues, courseNumberDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void update_ShouldReturnFalse_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        CourseNumber newValues = new CourseNumber();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("updated");

        Assertions.assertFalse(courseNumberDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldReturnTrue_WhenEntryIsDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");
        boolean isDeleted = courseNumberDao.deleteById(BigInteger.valueOf(4));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
        Assertions.assertTrue(isDeleted);
    }

    @Test
    public void deleteById_ShouldReturnFalse_WhenEntryNotDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");
        boolean isDeleted = courseNumberDao.deleteById(BigInteger.valueOf(6));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");

        Assertions.assertEquals(rowBefore, rowAfter);
        Assertions.assertFalse(isDeleted);
    }
}
