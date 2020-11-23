package ua.com.foxminded.studenthostel.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ua.com.foxminded.studenthostel.repository.EquipmentRepository;
import ua.com.foxminded.studenthostel.repository.FloorRepository;
import ua.com.foxminded.studenthostel.repository.RoomRepository;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.Room;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Optional;

@SpringBootTest
class RoomServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "AB-1234";
    public static final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

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
    private RoomRepository roomRepository;
    @Mock
    private FloorRepository floorRepository;
    @Mock
    private EquipmentRepository equipmentRepository;

    @BeforeEach
    public void initMock() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Room toDB = new Room();
        toDB.setName(VALID_NAME);
        toDB.setName(VALID_NAME);
        toDB.setFloor(getFloor(VALID_ID));

        Room fromDB = getRoom(VALID_ID, VALID_NAME);

        Mockito.when(roomRepository.save(toDB)).thenReturn(fromDB);
        Mockito.when(floorRepository.existsById(VALID_ID)).thenReturn(true);

        Assertions.assertEquals(VALID_ID, roomService.insert(toDB));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenNameNotValid() {
        Room room = new Room();
        room.setFloor(getFloor(VALID_ID));

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
        Room toDB = new Room();
        toDB.setName(VALID_NAME);

        toDB.setFloor(getFloor(ZERO_ID));
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(toDB));

        toDB.setFloor(getFloor(NEGATIVE_ID));
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(toDB));

        toDB.setFloor(getFloor(null));
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(toDB));
    }


    @Test
    public void insert_ShouldThrowExceptionWhen_FloorNotExist() {
        Room toDB = new Room();
        toDB.setName(VALID_NAME);
        toDB.setFloor(getFloor(VALID_ID));

        Mockito.when(floorRepository.existsById(VALID_ID)).thenReturn(false);
        Assertions.assertThrows(ValidationException.class, () -> roomService.insert(toDB));
    }

    @Test
    public void getById_ShouldReturnObject_WhenIdIsValid() {
        Room fromDB = getRoom(VALID_ID, VALID_NAME);

        Mockito.when(roomRepository.findById(VALID_ID)).thenReturn(Optional.of(fromDB));
        Assertions.assertEquals(fromDB, roomService.getById(VALID_ID));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> roomService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> roomService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> roomService.getById(null));
    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(roomRepository.findById(VALID_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> roomService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(roomRepository.findAll(PAGEABLE)).thenReturn(Page.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> roomService.getAll(0));
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
        Mockito.when(equipmentRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> roomService.getAllByEquipment(VALID_ID));
    }

    @Test
    public void getAllByEquipment_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(equipmentRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(roomRepository.getAllByEquipment(VALID_ID)).thenReturn(Collections.emptyList());

        Assertions.assertThrows(NotFoundException.class,
                () -> roomService.getAllByEquipment(VALID_ID));
    }


    @Test
    public void update_ShouldThrowException_WhenNameNotValid() {
        Room room = new Room();

        room.setId(VALID_ID);
        room.setFloor(getFloor(VALID_ID));

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
        Room room = getRoom(VALID_ID, VALID_NAME);

        room.setId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> roomService.update(room));

        room.setId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> roomService.update(room));

        room.setId(null);
        Assertions.assertThrows(ValidationException.class, () -> roomService.update(room));
    }

    @Test
    public void update_ShouldThrowException_WhenEntryNotExist() {
        Room room = getRoom(VALID_ID, VALID_NAME);

        Mockito.when(roomRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class, () -> roomService.update(room));
    }

    @Test
    public void update_ShouldThrowException_WhenFloorNotExist() {
        Room room = getRoom(VALID_ID, VALID_NAME);

        Mockito.when(floorRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class, () -> roomService.update(room));
    }

    @Test
    public void update_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> roomService.update(null));
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
        Mockito.when(roomRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> roomService.deleteById(VALID_ID));
    }

    private Room getRoom(BigInteger id, String name) {
        Room room = new Room();
        room.setId(id);
        room.setName(name);
        room.setFloor(getFloor(VALID_ID));

        return room;
    }

    private Floor getFloor(BigInteger id) {
        Floor floor = new Floor();
        floor.setName("name");
        floor.setId(id);

        return floor;
    }
}
