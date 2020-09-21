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
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Faculty;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class FacultyDaoTest {

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
        sqlScripts.addScript(new ClassPathResource("sql\\CreateTables.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);
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
        Assertions.assertEquals(BigInteger.valueOf(1), facultyDao.insert(faculty));
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
        faculty.setId(BigInteger.valueOf(1));
        faculty.setName("web design");
        Assertions.assertEquals(faculty, facultyDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getById_ShouldThrowException_WhenEntryNotExist() {

        Assertions.assertThrows(NotFoundException.class,
                () -> facultyDao.getById(BigInteger.valueOf(1)));
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

        Assertions.assertEquals(list, facultyDao.getAll(1, 3));
    }
    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFacultiesTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Faculty newValues = new Faculty();
        newValues.setId(BigInteger.valueOf(1));
        newValues.setName("newname");

        boolean isUpdated = facultyDao.update(newValues);

        Assertions.assertTrue(isUpdated);
        Assertions.assertEquals(newValues, facultyDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void update_ShouldReturnFalse_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFacultiesTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Faculty newValues = new Faculty();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("newname");

        Assertions.assertFalse(facultyDao.update(newValues));
    }
    @Test
    public void deleteById_ShouldReturnTrue_WhenEntryIsDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFacultiesTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");
        boolean isDeleted = facultyDao.deleteById(BigInteger.valueOf(5));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
        Assertions.assertTrue(isDeleted);
    }

    @Test
    public void deleteById_ShouldReturnFalse_WhenEntryNotDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFacultiesTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");
        boolean isDeleted = facultyDao.deleteById(BigInteger.valueOf(6));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");

        Assertions.assertEquals(rowBefore, rowAfter);
        Assertions.assertFalse(isDeleted);
    }
}
