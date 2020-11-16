package ua.com.foxminded.studenthostel.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.CourseNumber;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class CourseNumberDaoTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CourseNumberDao courseNumberDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static ResourceDatabasePopulator sqlScripts;

    @BeforeEach
    public void initScript() {
        sqlScripts = new ResourceDatabasePopulator();
    }

    @Test
    public void insert_ShouldMakeEntry_InCourseNumbersTable() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("First");

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");
        courseNumberDao.insert(courseNumber);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void insert_ShouldReturnId_WhenEntryIsInserted() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("First");

        Assertions.assertEquals(ONE, courseNumberDao.insert(courseNumber));
    }

    @Test
    public void insert_ShouldThrowException_WhenNameNotUnique() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("First");

        Assertions.assertThrows(DaoException.class, () -> courseNumberDao.insert(courseNumber));
    }

    @Test
    public void getById_ShouldReturnCorrectEntry_WhenIdIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(ONE);
        courseNumber.setName("First");

        Assertions.assertEquals(courseNumber, courseNumberDao.getById(ONE));
    }

    @Test
    public void getById_ShouldThrowException_WhenEntityNotExist() {

        Assertions.assertThrows(NotFoundException.class,
                () -> courseNumberDao.getById(ONE));
    }

    @Test
    public void getAll_ShouldReturnListOfCourseNumbers_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);


        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(BigInteger.valueOf(4));
        courseNumber.setName("Fourth");

        List<CourseNumber> courseNumbers = new ArrayList<>();
        courseNumbers.add(courseNumber);

        Assertions.assertEquals(courseNumbers, courseNumberDao.getAll(3, 1));
    }

    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        CourseNumber newValues = new CourseNumber();
        newValues.setId(ONE);
        newValues.setName("Updated");

        courseNumberDao.update(newValues);

        Assertions.assertEquals(newValues, courseNumberDao.getById(ONE));
    }

    @Test
    public void update_ShouldThrowException_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        CourseNumber newValues = new CourseNumber();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("Updated");

        Assertions.assertThrows(DaoException.class, () -> courseNumberDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");
        courseNumberDao.deleteById(BigInteger.valueOf(4));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }
}
