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
import ua.com.foxminded.studenthostel.repository.CourseNumberRepository;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.CourseNumber;

import java.math.BigInteger;
import java.util.Optional;

@SpringBootTest
class CourseNumberServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "Name";
    public static final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

    @Autowired
    @InjectMocks
    private CourseNumberService courseNumberService;
    @Mock
    private CourseNumberRepository repository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName(VALID_NAME);

        CourseNumber fromDb = getCourse(VALID_ID, VALID_NAME);

        Mockito.when(repository.save(courseNumber)).thenReturn(fromDb);
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
        CourseNumber courseNumber = getCourse(VALID_ID, VALID_NAME);

        Mockito.when(repository.findById(VALID_ID)).thenReturn(Optional.of(courseNumber));
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

        Mockito.when(repository.findById(VALID_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> courseNumberService.getById(VALID_ID));
    }


    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(repository.findAll(PAGEABLE)).thenReturn(Page.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> courseNumberService.getAll(0));
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

        Mockito.when(repository.existsById(VALID_ID)).thenReturn(false);

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
        Mockito.when(repository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> courseNumberService.deleteById(VALID_ID));
    }

    private CourseNumber getCourse(BigInteger id, String name) {

        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(id);
        courseNumber.setName(name);
        return courseNumber;
    }
}
