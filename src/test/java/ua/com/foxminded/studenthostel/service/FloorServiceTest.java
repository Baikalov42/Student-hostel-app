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
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Floor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class FloorServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "Name name";

    @Autowired
    @InjectMocks
    FloorService floorService;

    @Mock
    FloorDao floorDao;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Floor floor = new Floor();
        Mockito.when(floorDao.insert(floor)).thenReturn(VALID_ID);

        floor.setName("Name Name");
        Assertions.assertEquals(VALID_ID, floorService.insert(floor));

        floor.setName("Name NAME");
        Assertions.assertEquals(VALID_ID, floorService.insert(floor));

        floor.setName("NAme NAmE123");
        Assertions.assertEquals(VALID_ID, floorService.insert(floor));
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
        Floor floor = new Floor();
        floor.setName(VALID_NAME);
        floor.setId(VALID_ID);

        Mockito.when(floorDao.getById(VALID_ID)).thenReturn(floor);
        Assertions.assertEquals(floor, floorService.getById(VALID_ID));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> floorService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> floorService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> floorService.getById(null));
    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(floorDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> floorService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldReturnResultList_WhenConditionCompleted() {
        Floor floor = new Floor();
        floor.setName(VALID_NAME);
        floor.setId(VALID_ID);

        List<Floor> expectResult = new ArrayList<>();
        expectResult.add(floor);

        Mockito.when(floorDao.getAll(1, 10)).thenReturn(expectResult);
        Assertions.assertEquals(expectResult, floorService.getAll(1, 10));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(floorDao.getAll(10, 10)).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NotFoundException.class,
                () -> floorService.getAll(10, 10));
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

        Mockito.when(floorDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Floor floor = new Floor();
        floor.setName(VALID_NAME);
        floor.setId(VALID_ID);

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
        Mockito.when(floorDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> floorService.deleteById(VALID_ID));
    }
}
