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
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Room;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class RoomDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    RoomDao roomDao;

    @Autowired
    StudentDao studentDao;

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
    public void insert_ShouldMakeEntry_InRoomsTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room room = new Room();
        room.setId(BigInteger.valueOf(2));
        room.setName("testnametwo");
        room.setFloorId(BigInteger.valueOf(1));

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");
        roomDao.insert(room);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");

        Assertions.assertEquals(rowBefore + 1, rowAfter);
    }

    @Test
    public void insert_ShouldThrowException_WhenFloorIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room room = new Room();
        room.setId(BigInteger.valueOf(2));
        room.setName("testnametwo");
        room.setFloorId(BigInteger.valueOf(12));

        Assertions.assertThrows(DaoException.class,
                () -> roomDao.insert(room));
    }

    @Test
    public void getById_ShouldReturnRoom_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room expectRoom = new Room();
        expectRoom.setId(BigInteger.valueOf(1));
        expectRoom.setName("testroomone");
        expectRoom.setFloorId(BigInteger.valueOf(1));

        Assertions.assertEquals(expectRoom, roomDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);
        Assertions.assertThrows(NotFoundException.class,
                () -> roomDao.getById(BigInteger.valueOf(10)));
    }

    @Test
    public void getAll_ShouldReturnListOfRooms_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room room = new Room();
        room.setFloorId(BigInteger.valueOf(1));
        room.setName("testroomtwo");
        room.setId(BigInteger.valueOf(2));

        List<Room> list = new ArrayList<>();
        list.add(room);

        Assertions.assertEquals(list, roomDao.getAll(1, 1));
    }

    @Test
    public void getAllByEquipment_ShouldReturnListOfRRoms__WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room room = new Room();
        room.setId(BigInteger.valueOf(2));
        room.setName("testroomtwo");
        room.setFloorId(BigInteger.valueOf(1));

        List<Room> list = new ArrayList<>();
        list.add(room);

        Assertions.assertEquals(list, roomDao.getAllByEquipment(BigInteger.valueOf(1)));
    }
    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room newValues = new Room();
        newValues.setId(BigInteger.valueOf(1));
        newValues.setName("newname");
        newValues.setFloorId(BigInteger.valueOf(1));

        boolean isUpdated = roomDao.update(newValues);

        Assertions.assertTrue(isUpdated);
        Assertions.assertEquals(newValues, roomDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void update_ShouldReturnFalse_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room newValues = new Room();
        newValues.setId(BigInteger.valueOf(4));
        newValues.setName("newname");
        newValues.setFloorId(BigInteger.valueOf(1));

        Assertions.assertFalse(roomDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldReturnTrue_WhenEntryIsDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");
        boolean isDeleted = roomDao.deleteById(BigInteger.valueOf(1));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
        Assertions.assertTrue(isDeleted);
    }

    @Test
    public void deleteById_ShouldReturnFalse_WhenEntryNotDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");
        boolean isDeleted = roomDao.deleteById(BigInteger.valueOf(3));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");

        Assertions.assertEquals(rowBefore, rowAfter);
        Assertions.assertFalse(isDeleted);
    }
}
