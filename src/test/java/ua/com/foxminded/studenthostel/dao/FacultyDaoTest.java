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
import ua.com.foxminded.studenthostel.models.Faculty;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringJUnitConfig(SpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FacultyDaoTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private DataSource dataSource;

    @Autowired
    FacultyDao facultyDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static ResourceDatabasePopulator sqlScripts;

    @BeforeEach
    public void addTablesScript() {
        sqlScripts = new ResourceDatabasePopulator();
    }

    @Test
    public void insert_ShouldMakeEntry_InFacultiesTable() {
        Faculty faculty = new Faculty();
        faculty.setName("web design");

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");
        facultyDao.insert(faculty);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void insert_ShouldReturnId_WhenEntryIsInserted() {
        Faculty faculty = new Faculty();
        faculty.setName("web design");
        Assertions.assertEquals(ONE, facultyDao.insert(faculty));
    }

    @Test
    public void insert_ShouldThrowException_WhenNameNotUnique() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFacultiesTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Faculty faculty = new Faculty();
        faculty.setName("web design");

        Assertions.assertThrows(DaoException.class, () -> facultyDao.insert(faculty));
    }

    @Test
    public void getById_ShouldReturnFaculty_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFacultiesTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Faculty faculty = new Faculty();
        faculty.setId(ONE);
        faculty.setName("web design");
        Assertions.assertEquals(faculty, facultyDao.getById(ONE));
    }

    @Test
    public void getById_ShouldThrowException_WhenEntryNotExist() {

        Assertions.assertThrows(NotFoundException.class,
                () -> facultyDao.getById(ONE));
    }

    @Test
    public void getAll_ShouldReturnListOfFaculties_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFacultiesTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Faculty faculty = new Faculty();
        faculty.setId(BigInteger.valueOf(4));
        faculty.setName("front end development");

        List<Faculty> list = new ArrayList<>();
        list.add(faculty);

        Assertions.assertEquals(list, facultyDao.getAll(3, 1));
    }

    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFacultiesTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Faculty newValues = new Faculty();
        newValues.setId(ONE);
        newValues.setName("newname");

        facultyDao.update(newValues);

        Assertions.assertEquals(newValues, facultyDao.getById(ONE));
    }

    @Test
    public void update_ShouldThrowException_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFacultiesTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Faculty newValues = new Faculty();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("newname");

        Assertions.assertThrows(DaoException.class, () -> facultyDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFacultiesTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");
        facultyDao.deleteById(ONE);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }
}
