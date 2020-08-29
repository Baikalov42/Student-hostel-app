package ua.com.foxminded.studenthostel.dao;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.Student;

import javax.sql.DataSource;
import java.math.BigInteger;

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
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomTable.sql"));
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
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room room = new Room();
        room.setId(BigInteger.valueOf(2));
        room.setName("testnametwo");
        room.setFloorId(BigInteger.valueOf(12));

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> roomDao.insert(room));
    }

    @Test
    public void getById_ShouldReturnRoom_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room expectRoom = new Room();
        expectRoom.setId(BigInteger.valueOf(1));
        expectRoom.setName("testroomone");
        expectRoom.setFloorId(BigInteger.valueOf(1));

        Assertions.assertEquals(expectRoom, roomDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);
        Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> roomDao.getById(BigInteger.valueOf(10)));
    }

    @Test
    public void changeRoom_ShouldChangeRoomOnStudent_WhenStudentIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        BigInteger newRoomId = BigInteger.valueOf(2);
        Student expect = studentDao.getById(BigInteger.valueOf(1));
        expect.setRoomId(newRoomId);

        roomDao.changeRoom(newRoomId, expect.getId());
        Student actual = studentDao.getById(BigInteger.valueOf(1));

        Assertions.assertEquals(expect, actual);
    }

    @Test
    public void changeRoom_ShouldThrowException_WhenNewRoomIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> roomDao.changeRoom(BigInteger.valueOf(10), BigInteger.valueOf(1)));
    }

    @Test
    public void deleteById_ShouldDeleteEntry_FromRoomsTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");
        roomDao.deleteById(BigInteger.valueOf(1));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }
}
