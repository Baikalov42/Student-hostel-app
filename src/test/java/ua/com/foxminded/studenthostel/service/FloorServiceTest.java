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
import ua.com.foxminded.studenthostel.repository.FloorRepository;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Floor;

import java.math.BigInteger;
import java.util.Optional;

@SpringBootTest
class FloorServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "Name name";
    public static final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

    @Autowired
    @InjectMocks
    private FloorService floorService;

    @Mock
    private FloorRepository floorRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Floor toDB = new Floor();
        Floor fromDB = getFloor(VALID_ID, VALID_NAME);

        Mockito.when(floorRepository.save(toDB)).thenReturn(fromDB);

        toDB.setName("Name Name");
        Assertions.assertEquals(VALID_ID, floorService.insert(toDB));

        toDB.setName("Name NAME");
        Assertions.assertEquals(VALID_ID, floorService.insert(toDB));

        toDB.setName("NAme NAmE123");
        Assertions.assertEquals(VALID_ID, floorService.insert(toDB));
    }


    @Test
    public void insert_ShouldThrowExceptionWhenNameNotValid() {
        Floor floor = new Floor();

        floor.setName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName("Notvalid ");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName("Not  valid");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName("Not+valid");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName("Not  valid vkvkvkvkvkvkvkvkvkvkv");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName("n");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName("");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName("   ");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> floorService.insert(null));
    }

    @Test
    public void getById_ShouldReturnObject_WhenIdIsValid() {
        Floor fromDB = getFloor(VALID_ID, VALID_NAME);

        Mockito.when(floorRepository.findById(VALID_ID)).thenReturn(Optional.of(fromDB));
        Assertions.assertEquals(fromDB, floorService.getById(VALID_ID));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> floorService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> floorService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> floorService.getById(null));
    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(floorRepository.findById(VALID_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> floorService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(floorRepository.findAll(PAGEABLE)).thenReturn(Page.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> floorService.getAll(0));
    }


    @Test
    public void update_ShouldThrowException_WhenNameNotValid() {
        Floor floor = new Floor();
        floor.setId(VALID_ID);

        floor.setName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> floorService.update(floor));

        floor.setName("Notvalid ");
        Assertions.assertThrows(ValidationException.class, () -> floorService.update(floor));

        floor.setName("Not  valid");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName("Not+valid");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName("Not  valid vkvkvkvkvkvkvkvkvkvkv");
        Assertions.assertThrows(ValidationException.class, () -> floorService.insert(floor));

        floor.setName("n");
        Assertions.assertThrows(ValidationException.class, () -> floorService.update(floor));

        floor.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> floorService.update(floor));

        floor.setName("");
        Assertions.assertThrows(ValidationException.class, () -> floorService.update(floor));

        floor.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> floorService.update(floor));

        floor.setName("   ");
        Assertions.assertThrows(ValidationException.class, () -> floorService.update(floor));

        floor.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> floorService.update(floor));
    }

    @Test
    public void update_ShouldThrowException_WhenIdNotValid() {
        Floor floor = new Floor();
        floor.setName(VALID_NAME);

        floor.setId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> floorService.update(floor));

        floor.setId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> floorService.update(floor));
    }

    @Test
    public void update_ShouldThrowException_WhenEntryNotExist() {

        Mockito.when(floorRepository.existsById(VALID_ID)).thenReturn(false);

        Floor floor = getFloor(VALID_ID, VALID_NAME);

        Assertions.assertThrows(ValidationException.class, () -> floorService.update(floor));
    }

    @Test
    public void update_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> floorService.update(null));
    }


    @Test
    public void deleteById_ShouldThrowException_WhenIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> floorService.deleteById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> floorService.deleteById(ZERO_ID));


        Assertions.assertThrows(ValidationException.class,
                () -> floorService.deleteById(null));
    }

    @Test
    public void deleteById_ShouldThrowException_WhenEntryNotExist() {
        Mockito.when(floorRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> floorService.deleteById(VALID_ID));
    }

    private Floor getFloor(BigInteger id, String name) {
        Floor floor = new Floor();
        floor.setId(id);
        floor.setName(name);
        return floor;
    }
}
