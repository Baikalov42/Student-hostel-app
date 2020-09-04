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
import ua.com.foxminded.studenthostel.models.Group;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class GroupDaoTest {

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
        sqlScripts.addScript(new ClassPathResource("sql\\CreateTables.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);
    }

    @Test
    public void insert_ShouldMakeEntryInGroupsTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group group = new Group();

        group.setCourseNumberId(BigInteger.valueOf(1));
        group.setName("test");
        group.setFacultyId(BigInteger.valueOf(1));
        group.setId(BigInteger.valueOf(2));

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

        group.setId(BigInteger.valueOf(1));
        group.setName("testname");
        group.setCourseNumberId(BigInteger.valueOf(1));
        group.setFacultyId(BigInteger.valueOf(2));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> groupDao.insert(group));
    }

    @Test
    public void insert_ShouldThrowException_WhenCourseNumberIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group group = new Group();

        group.setId(BigInteger.valueOf(1));
        group.setName("testname");
        group.setCourseNumberId(BigInteger.valueOf(2));
        group.setFacultyId(BigInteger.valueOf(1));

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> groupDao.insert(group));
    }

    @Test
    public void getById_ShouldReturnCorrectEntry_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group expectGroup = new Group();

        expectGroup.setId(BigInteger.valueOf(1));
        expectGroup.setName("testname1");
        expectGroup.setFacultyId(BigInteger.valueOf(1));
        expectGroup.setCourseNumberId(BigInteger.valueOf(1));

        Assertions.assertEquals(expectGroup, groupDao.getById(BigInteger.valueOf(1)));

    }

    @Test
    public void getById_ShouldThrowException_WhenEntryNotExist() {
        Assertions.assertThrows(DaoException.class,
                () -> groupDao.getById(BigInteger.valueOf(1)));
    }
    @Test
    public void getAll_ShouldReturnListOfGroups_WhenConditionCompleted(){
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group group = new Group();
        group.setId(BigInteger.valueOf(2));
        group.setName("testname2");
        group.setCourseNumberId(BigInteger.valueOf(1));
        group.setFacultyId(BigInteger.valueOf(1));

        List<Group> list = new ArrayList<>();
        list.add(group);
        Assertions.assertEquals(list, groupDao.getAll(1,1));
    }
    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group newValues = new Group();
        newValues.setId(BigInteger.valueOf(1));
        newValues.setName("newname");
        newValues.setCourseNumberId(BigInteger.valueOf(1));
        newValues.setFacultyId(BigInteger.valueOf(1));

        boolean isUpdated = groupDao.update(newValues);

        Assertions.assertTrue(isUpdated);
        Assertions.assertEquals(newValues, groupDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void update_ShouldReturnFalse_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group newValues = new Group();
        newValues.setId(BigInteger.valueOf(4));
        newValues.setName("newname");
        newValues.setCourseNumberId(BigInteger.valueOf(1));
        newValues.setFacultyId(BigInteger.valueOf(1));

        Assertions.assertFalse(groupDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldReturnTrue_WhenEntryIsDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        boolean isDeleted = groupDao.deleteById(BigInteger.valueOf(1));
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");

        Assertions.assertEquals(rowNumberBefore - 1, rowNumberAfter);
        Assertions.assertTrue(isDeleted);
    }
    @Test
    public void deleteById_ShouldReturnFalse_WhenEntryNotDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToGroupsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        boolean isDeleted = groupDao.deleteById(BigInteger.valueOf(4));
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");

        Assertions.assertEquals(rowNumberBefore , rowNumberAfter);
        Assertions.assertFalse(isDeleted);
    }
}