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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Floor;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class FloorDaoTest {

    private static final BigInteger ONE = BigInteger.ONE;

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
        floor.setName("Floor");

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
        floor.setId(ONE);
        floor.setName("Floor");

        Assertions.assertEquals(floor, floorDao.getById(ONE));

    }

    @Test
    public void getById_ShouldThrowException_WhenEntryNotExist() {
        Assertions.assertThrows(NotFoundException.class, () -> floorDao.getById(ONE));
    }

    @Test
    public void getAll_ShouldReturnListOfFloor_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFloorsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        List<Floor> list = new ArrayList<>();

        Floor floor = new Floor();
        floor.setName("Floorthree");
        floor.setId(BigInteger.valueOf(3));

        list.add(floor);
        Assertions.assertEquals(list, floorDao.getAll(2, 1));
    }

    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFloorsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Floor newValues = new Floor();
        newValues.setId(ONE);
        newValues.setName("Newname");

        floorDao.update(newValues);

        Assertions.assertEquals(newValues, floorDao.getById(ONE));
    }

    @Test
    public void update_ShouldThrowException_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFloorsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Floor newValues = new Floor();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("Newname");

        Assertions.assertThrows(DaoException.class, () -> floorDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToFloorsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");
        floorDao.deleteById(ONE);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");

        Assertions.assertEquals(rowNumberBefore - 1, rowNumberAfter);
    }
}
