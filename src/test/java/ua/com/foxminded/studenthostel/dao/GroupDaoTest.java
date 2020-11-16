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
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.Group;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class GroupDaoTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private DataSource dataSource;

    @Autowired
    GroupDao groupDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static ResourceDatabasePopulator sqlScripts;

    @BeforeEach
    public void addTablesScript() {
        sqlScripts = new ResourceDatabasePopulator();
    }

    @Test
    public void insert_ShouldMakeEntryInGroupsTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group group = new Group();

        group.setCourseNumber(getCourse());
        group.setName("GRP-1234");
        group.setFaculty(getFaculty());

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        groupDao.insert(group);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void insert_ShouldThrowException_WhenFacultyIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group group = new Group();

        group.setId(ONE);
        group.setName("GRP-1234");
        group.setCourseNumber(getCourse());
        group.setFaculty(getFaculty());

        Assertions.assertThrows(DaoException.class, () -> groupDao.insert(group));
    }

    @Test
    public void insert_ShouldThrowException_WhenCourseNumberIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group group = new Group();

        group.setId(ONE);
        group.setName("GRP-1234");
        group.setCourseNumber(getCourse());
        group.setFaculty(getFaculty());

        Assertions.assertThrows(DaoException.class, () -> groupDao.insert(group));
    }

    @Test
    public void getById_ShouldReturnCorrectEntry_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group expectGroup = new Group();

        expectGroup.setId(BigInteger.valueOf(2));
        expectGroup.setName("GRP-1111");
        expectGroup.setFaculty(getFaculty());
        expectGroup.setCourseNumber(getCourse());

        Assertions.assertEquals(expectGroup, groupDao.getById(BigInteger.valueOf(2)));

    }

    @Test
    public void getById_ShouldThrowException_WhenEntryNotExist() {
        Assertions.assertThrows(NotFoundException.class,
                () -> groupDao.getById(ONE));
    }

    @Test
    public void getAll_ShouldReturnListOfGroups_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group group = new Group();
        group.setId(BigInteger.valueOf(3));
        group.setName("GRP-2222");
        group.setCourseNumber(getCourse());
        group.setFaculty(getFaculty());

        List<Group> list = new ArrayList<>();
        list.add(group);
        Assertions.assertEquals(list, groupDao.getAll(1, 1));
    }

    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group newValues = new Group();
        newValues.setId(BigInteger.valueOf(2));
        newValues.setName("NEW-1111");
        newValues.setCourseNumber(getCourse());
        newValues.setFaculty(getFaculty());

        groupDao.update(newValues);

        Assertions.assertEquals(newValues, groupDao.getById(BigInteger.valueOf(2)));
    }
    
    @Test
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        groupDao.deleteById(BigInteger.valueOf(2));
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");

        Assertions.assertEquals(rowNumberBefore - 1, rowNumberAfter);

    }

    CourseNumber getCourse() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("Course");
        courseNumber.setId(ONE);

        return courseNumber;
    }

    Faculty getFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Faculty");
        faculty.setId(ONE);

        return faculty;
    }
}
