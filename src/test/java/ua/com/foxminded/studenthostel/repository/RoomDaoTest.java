package ua.com.foxminded.studenthostel.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.Room;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomDaoTest {

    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TWO = BigInteger.valueOf(2);
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql("/sql/AddDataToRoomsTable.sql")
    public void insert_ShouldMakeEntry_InRoomsTable() {

        Room room = new Room();
        room.setId(ONE);
        room.setName("RM-0001");
        room.setFloor(getFloor());

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");
        roomRepository.saveAndFlush(room);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");

        Assertions.assertEquals(rowBefore + 1, rowAfter);
    }

    @Test
    @Sql("/sql/AddDataToRoomsTable.sql")
    public void insert_ShouldThrowException_WhenFloorIdNotExist() {
        Floor notExist = new Floor();
        notExist.setId(BigInteger.valueOf(10));
        notExist.setName("Notexist");

        Room room = new Room();
        room.setId(BigInteger.valueOf(2));
        room.setName("RM-0002");
        room.setFloor(notExist);

        Assertions.assertThrows(DataAccessException.class,
                () -> roomRepository.saveAndFlush(room));
    }

    @Test
    @Sql("/sql/AddDataToRoomsTable.sql")
    public void getById_ShouldReturnRoom_WhenEntryIsExist() {

        Room expectRoom = new Room();
        expectRoom.setId(TWO);
        expectRoom.setName("RM-0002");
        expectRoom.setFloor(getFloor());

        Assertions.assertEquals(expectRoom, roomRepository.findById(TWO).get());
    }

    @Test
    @Sql("/sql/AddDataToRoomsTable.sql")
    public void getAll_ShouldReturnListOfRooms_WhenConditionCompleted() {

        Room room = new Room();
        room.setFloor(getFloor());
        room.setName("RM-0003");
        room.setId(BigInteger.valueOf(3));

        List<Room> list = new ArrayList<>();
        list.add(room);

        Pageable pageable = PageRequest.of(1, 1, Sort.Direction.ASC, "id");
        Assertions.assertEquals(list, roomRepository.findAll(pageable).getContent());
    }

    @Test
    @Sql("/sql/AddDataToRoomsTable.sql")
    public void getAllByEquipment_ShouldReturnListOfRRoms__WhenConditionCompleted() {

        Room room = new Room();
        room.setId(BigInteger.valueOf(3));
        room.setName("RM-0003");
        room.setFloor(getFloor());

        List<Room> list = new ArrayList<>();
        list.add(room);

        Assertions.assertEquals(list, roomRepository.getAllByEquipment(ONE));
    }

    @Test
    @Sql("/sql/AddDataToRoomsTable.sql")
    public void update_ShouldUpdateEntry_WhenDataExist() {

        Room newValues = new Room();
        newValues.setId(TWO);
        newValues.setName("NW-0002");
        newValues.setFloor(getFloor());

        roomRepository.saveAndFlush(newValues);

        Assertions.assertEquals(newValues, roomRepository.findById(TWO).get());
    }

    @Test
    @Sql("/sql/AddDataToRoomsTable.sql")
    public void update_ShouldThrowException_WhenDataNotExist() {

        Room newValues = new Room();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("NW-0007");
        newValues.setFloor(getFloor());

        Assertions.assertThrows(DataAccessException.class,
                () -> roomRepository.saveAndFlush(newValues));
    }

    @Test
    @Sql("/sql/AddDataToRoomsTable.sql")
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");
        roomRepository.deleteById(BigInteger.valueOf(4));
        roomRepository.flush();
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "rooms");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }

    Floor getFloor() {

        Floor floor = new Floor();
        floor.setId(ONE);
        floor.setName("Floor");
        return floor;
    }
}
