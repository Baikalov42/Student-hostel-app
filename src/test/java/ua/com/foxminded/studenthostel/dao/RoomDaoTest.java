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
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.Room;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringJUnitConfig(SpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomDaoTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @PersistenceContext
    private EntityManager entityManager;

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
    }


    @Test
    public void insert_ShouldMakeEntry_InRoomsTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room room = new Room();
        room.setId(BigInteger.valueOf(2));
        room.setName("testnametwo");
        room.setFloor(getFloor());

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
        room.setFloor(getFloor());

        Assertions.assertThrows(DaoException.class,
                () -> roomDao.insert(room));
    }

    @Test
    public void getById_ShouldReturnRoom_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room expectRoom = new Room();
        expectRoom.setId(ONE);
        expectRoom.setName("testroomone");
        expectRoom.setFloor(getFloor());

        Assertions.assertEquals(expectRoom, roomDao.getById(ONE));
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
        room.setFloor(getFloor());
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
        room.setFloor(getFloor());

        List<Room> list = new ArrayList<>();
        list.add(room);

        Assertions.assertEquals(list, roomDao.getAllByEquipment(ONE));
    }

    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room newValues = new Room();
        newValues.setId(ONE);
        newValues.setName("newname");
        newValues.setFloor(getFloor());

        roomDao.update(newValues);

        Assertions.assertEquals(newValues, roomDao.getById(ONE));
    }

    @Test
    public void update_ShouldThrowException_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room newValues = new Room();
        newValues.setId(BigInteger.valueOf(4));
        newValues.setName("newname");
        newValues.setFloor(getFloor());

        Assertions.assertThrows(DaoException.class, () -> roomDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");
        roomDao.deleteById(BigInteger.valueOf(3));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }

    Floor getFloor() {

        Floor floor = entityManager.find(Floor.class, ONE);
        return floor;
    }
}
