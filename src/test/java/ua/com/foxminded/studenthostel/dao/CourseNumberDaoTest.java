package ua.com.foxminded.studenthostel.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.CourseNumber;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void getById_ShouldReturnCorrectEntry_WhenIdIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        CourseNumber courseNumber = CourseNumber.FIRST;
        Assertions.assertEquals(courseNumber, courseNumberDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getById_ShouldThrowException_WhenEntityNotExist() {

        Assertions.assertThrows(DaoException.class,
                () -> courseNumberDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getAll_ShouldReturnListOfCourseNumbers() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToCourseNumbersTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        List<CourseNumber> courseNumbers = new ArrayList<>();
        courseNumbers.add(CourseNumber.FOURTH);
        Assertions.assertEquals(courseNumbers, courseNumberDao.getAll(1, 3));
    }
}
