package ua.com.foxminded.studenthostel.dao;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.models.Faculty;

import javax.sql.DataSource;
import java.math.BigInteger;

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
    public void save_ShouldMakeEntry_InCourseNumbersTable() {
        Faculty faculty = Faculty.WEB_DESIGN;
        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");
        facultyDao.save(faculty);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");
        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);

    }

    @Test
    public void getById_ShouldReturnFaculty_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFacultiesTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Faculty faculty = Faculty.WEB_DESIGN;
        Assertions.assertEquals(faculty, facultyDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getById_ShouldThrowException_WhenEntryNotExist() {

        Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> facultyDao.getById(BigInteger.valueOf(1)));
    }
}
