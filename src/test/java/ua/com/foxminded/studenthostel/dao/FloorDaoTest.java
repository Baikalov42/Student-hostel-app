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
import ua.com.foxminded.studenthostel.models.Floor;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class FloorDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    FloorDao floorDao;

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
    public void insert_ShouldMakeEntry_InFloorsTable() {
        Floor floor = new Floor();
        floor.setId(BigInteger.valueOf(1));
        floor.setName("testfloor");

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");
        floorDao.insert(floor);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void getById_ShouldReturnFloor_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFloorsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Floor floor = new Floor();
        floor.setId(BigInteger.valueOf(1));
        floor.setName("testfloor");

        Assertions.assertEquals(floor, floorDao.getById(BigInteger.valueOf(1)));

    }

    @Test
    public void getById_ShouldThrowException_WhenEntryNotExist() {
        Assertions.assertThrows(NotFoundException.class, () -> floorDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getAll_ShouldReturnListOfFloor_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFloorsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        List<Floor> list = new ArrayList<>();

        Floor floor = new Floor();
        floor.setName("testfloor3");
        floor.setId(BigInteger.valueOf(3));

        list.add(floor);
        Assertions.assertEquals(list, floorDao.getAll(1, 2));
    }
    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFloorsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Floor newValues = new Floor();
        newValues.setId(BigInteger.valueOf(1));
        newValues.setName("newname");

        boolean isUpdated = floorDao.update(newValues);

        Assertions.assertTrue(isUpdated);
        Assertions.assertEquals(newValues, floorDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void update_ShouldReturnFalse_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFloorsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Floor newValues = new Floor();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("newname");

        Assertions.assertFalse(floorDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldReturnTrue_WhenEntryIsDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFloorsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");
        boolean isDeleted = floorDao.deleteById(BigInteger.valueOf(1));
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");

        Assertions.assertEquals(rowNumberBefore - 1, rowNumberAfter);
        Assertions.assertTrue(isDeleted);
    }

    @Test
    public void deleteById_ShouldReturnFalse_WhenEntryNotDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFloorsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");
        boolean isDeleted = floorDao.deleteById(BigInteger.valueOf(5));
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");

        Assertions.assertEquals(rowNumberBefore, rowNumberAfter);
        Assertions.assertFalse(isDeleted);
    }
}
