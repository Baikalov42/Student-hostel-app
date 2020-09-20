package ua.com.foxminded.studenthostel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.dao.EquipmentDao;
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.dao.RoomDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.Room;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class RoomServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "AB-1234";

    @Autowired
    @InjectMocks
    private RoomService roomService;

    @InjectMocks
    @Autowired
    private EquipmentService equipmentService;

    @InjectMocks
    @Autowired
    private FloorService floorService;

    @Mock
    private RoomDao roomDao;
    @Mock
    private FloorDao floorDao;
    @Mock
    private EquipmentDao equipmentDao;

    @BeforeEach
    public void initMock() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Room room = new Room();

        room.setName(VALID_NAME);
        room.setFloorId(VALID_ID);

        Mockito.when(roomDao.insert(room)).thenReturn(VALID_ID);
        Mockito.when(floorDao.getById(VALID_ID)).thenReturn(new Floor());

        Assertions.assertEquals(VALID_ID, roomService.insert(room));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenNameNotValid() {
        Room room = new Room();
        room.setFloorId(VALID_ID);

        room.setName("");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("1");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("A");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("a");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("AB-123");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("Ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("AB_1234");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("AB-12345");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

    }

    @Test
    public void insert_ShouldThrowExceptionWhen_FloorIdNotValid() {
        Room room = new Room();
        room.setName(VALID_NAME);
        room.setFloorId(VALID_ID);

        room.setFloorId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setFloorId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setFloorId(null);
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));
    }


    @Test
    public void insert_ShouldThrowExceptionWhen_FloorNotExist() {
        Room room = new Room();
        room.setName(VALID_NAME);
        room.setFloorId(VALID_ID);

        Mockito.when(floorDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));
    }

    @Test
    public void getById_ShouldReturnObject_WhenIdIsValid() {
        Room room = new Room();
        room.setName(VALID_NAME);

        Mockito.when(roomDao.getById(VALID_ID)).thenReturn(room);
        Assertions.assertEquals(room, roomService.getById(VALID_ID));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> roomService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> roomService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> roomService.getById(null));
    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(roomDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> roomService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldReturnResultList_WhenConditionCompleted() {
        Room room = new Room();
        room.setId(VALID_ID);
        room.setName(VALID_NAME);
        room.setFloorId(VALID_ID);

        List<Room> expectResult = new ArrayList<>();
        expectResult.add(room);

        Mockito.when(roomDao.getAll(1, 10)).thenReturn(expectResult);
        Assertions.assertEquals(expectResult, roomService.getAll(1, 10));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(roomDao.getAll(10, 10)).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NotFoundException.class,
                () -> roomService.getAll(10, 10));
    }

    @Test
    public void getAllByEquipment_ShouldThrowException_WhenIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> roomService.getAllByEquipment(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> roomService.getAllByEquipment(ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> roomService.getAllByEquipment(null));
    }

    @Test
    public void getAllByEquipment_ShouldThrowException_WhenEquipmentNotExist() {
        Mockito.when(equipmentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> roomService.getAllByEquipment(VALID_ID));
    }

    @Test
    public void getAllByEquipment_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(roomDao.getAllByEquipment(VALID_ID)).thenReturn(Collections.emptyList());

        Assertions.assertThrows(NotFoundException.class,
                () -> roomService.getAllByEquipment(VALID_ID));
    }

    @Test
    public void update_ShouldReturnTrue_WhenEntryIsUpdated() {
        Room room = new Room();

        room.setId(VALID_ID);
        room.setName(VALID_NAME);
        room.setFloorId(VALID_ID);

        Mockito.when(roomDao.getById(VALID_ID)).thenReturn(new Room());
        Mockito.when(floorDao.getById(VALID_ID)).thenReturn(new Floor());

        Mockito.when(roomDao.update(room)).thenReturn(true);
        Assertions.assertTrue(roomService.update(room));
    }

    @Test
    public void update_ShouldThrowException_WhenNameNotValid() {
        Room room = new Room();

        room.setId(VALID_ID);
        room.setFloorId(VALID_ID);

        room.setName("");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("1");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("A");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("a");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("AB-123");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("Ab-1234");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("AB_1234");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName("AB-12345");
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

        room.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(room));

    }

    @Test
    public void update_ShouldThrowException_WhenIdNotValid() {
        Room room = new Room();

        room.setName(VALID_NAME);
        room.setFloorId(VALID_ID);

        room.setId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> roomService.update(room));

        room.setId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> roomService.update(room));

        room.setId(null);
        Assertions.assertThrows(ValidationException.class, () -> roomService.update(room));
    }

    @Test
    public void update_ShouldThrowException_WhenEntryNotExist() {
        Room room = new Room();
        room.setId(VALID_ID);
        room.setName(VALID_NAME);
        room.setFloorId(VALID_ID);

        Mockito.when(roomDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> roomService.update(room));
    }

    @Test
    public void update_ShouldThrowException_WhenFacultyNotExist() {
        Room room = new Room();
        room.setId(VALID_ID);
        room.setName(VALID_NAME);
        room.setFloorId(VALID_ID);

        Mockito.when(floorDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class, () -> roomService.update(room));
    }

    @Test
    public void update_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> roomService.update(null));
    }

    @Test
    public void deleteById_ShouldReturnTrue_WhenEntryIsDeleted() {
        Mockito.when(roomDao.deleteById(VALID_ID)).thenReturn(true);
        Mockito.when(roomDao.getById(VALID_ID)).thenReturn(new Room());

        Assertions.assertTrue(roomService.deleteById(VALID_ID));
    }

    @Test
    public void deleteById_ShouldThrowException_WhenIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> roomService.deleteById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> roomService.deleteById(BigInteger.ZERO));

        Assertions.assertThrows(ValidationException.class,
                () -> roomService.deleteById(null));
    }

    @Test
    public void deleteById_ShouldThrowException_WhenEntryNotExist() {
        Mockito.when(roomDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> roomService.deleteById(VALID_ID));
    }
}
