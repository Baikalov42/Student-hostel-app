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
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.CourseNumber;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class CourseNumberServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "Name";

    @Autowired
    @InjectMocks
    private CourseNumberService courseNumberService;
    @Mock
    private CourseNumberDao courseNumberDao;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName(VALID_NAME);

        Mockito.when(courseNumberDao.insert(courseNumber)).thenReturn(VALID_ID);
        Assertions.assertEquals(VALID_ID, courseNumberService.insert(courseNumber));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenNameNotValid() {
        CourseNumber courseNumber = new CourseNumber();

        courseNumber.setName("NOTVALID");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.insert(courseNumber));

        courseNumber.setName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.insert(courseNumber));

        courseNumber.setName("Not valid");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.insert(courseNumber));

        courseNumber.setName("NotValid");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.insert(courseNumber));

        courseNumber.setName("n");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.insert(courseNumber));

        courseNumber.setName("Not");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.insert(courseNumber));

        courseNumber.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.insert(courseNumber));

        courseNumber.setName("");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.insert(courseNumber));

        courseNumber.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.insert(courseNumber));

        courseNumber.setName("   ");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.insert(courseNumber));

        courseNumber.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.insert(courseNumber));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> courseNumberService.insert(null));
    }

    @Test
    public void getById_ShouldReturnObject_WhenIdIsValid() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName(VALID_NAME);
        courseNumber.setId(VALID_ID);

        Mockito.when(courseNumberDao.getById(VALID_ID)).thenReturn(courseNumber);
        Assertions.assertEquals(courseNumber, courseNumberService.getById(VALID_ID));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.deleteById(null));
    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(courseNumberDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> courseNumberService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldReturnResultList_WhenConditionCompleted() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName(VALID_NAME);
        courseNumber.setId(VALID_ID);

        List<CourseNumber> expectResult = new ArrayList<>();
        expectResult.add(courseNumber);

        Mockito.when(courseNumberDao.getAll(1, 10)).thenReturn(expectResult);
        Assertions.assertEquals(expectResult, courseNumberService.getAll(1, 10));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(courseNumberDao.getAll(10, 10)).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NotFoundException.class,
                () -> courseNumberService.getAll(10, 10));
    }


    @Test
    public void update_ShouldThrowException_WhenNameNotValid() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(VALID_ID);

        courseNumber.setName("NOTVALID");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));

        courseNumber.setName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));

        courseNumber.setName("Not valid");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));

        courseNumber.setName("NotValid");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));

        courseNumber.setName("n");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));

        courseNumber.setName("Not");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));

        courseNumber.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));

        courseNumber.setName("");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));

        courseNumber.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));

        courseNumber.setName("   ");
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));

        courseNumber.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));
    }

    @Test
    public void update_ShouldThrowException_WhenIdNotValid() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName(VALID_NAME);

        courseNumber.setId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));

        courseNumber.setId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));
    }

    @Test
    public void update_ShouldThrowException_WhenEntryNotExist() {

        Mockito.when(courseNumberDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName(VALID_NAME);
        courseNumber.setId(VALID_ID);

        Assertions.assertThrows(ValidationException.class, () -> courseNumberService.update(courseNumber));
    }

    @Test
    public void update_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> courseNumberService.update(null));
    }


    @Test
    public void deleteById_ShouldThrowException_WhenIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> courseNumberService.deleteById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> courseNumberService.deleteById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> courseNumberService.deleteById(null));
    }

    @Test
    public void deleteById_ShouldThrowException_WhenEntryNotExist() {
        Mockito.when(courseNumberDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> courseNumberService.deleteById(VALID_ID));
    }
}
