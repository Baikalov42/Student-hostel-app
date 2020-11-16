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
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Faculty;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class FacultyServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "Name name";

    @Autowired
    @InjectMocks
    FacultyService facultyService;

    @Mock
    FacultyDao facultyDao;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Faculty faculty = new Faculty();
        Mockito.when(facultyDao.insert(faculty)).thenReturn(VALID_ID);

        faculty.setName("Name Name");
        Assertions.assertEquals(BigInteger.ONE, facultyService.insert(faculty));

        faculty.setName("Name NAME");
        Assertions.assertEquals(BigInteger.ONE, facultyService.insert(faculty));

        faculty.setName("NAme NAmE123");
        Assertions.assertEquals(BigInteger.ONE, facultyService.insert(faculty));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenNameNotValid() {
        Faculty faculty = new Faculty();

        faculty.setName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName("Notvalid ");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName("Not  valid");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName("Not+valid");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName("Not  valid vkvkvkvkvkvkvkvkvkvkv");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName("n");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName("");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName("   ");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> facultyService.insert(null));
    }

    @Test
    public void getById_ShouldReturnObject_WhenIdIsValid() {
        Faculty faculty = new Faculty();
        faculty.setName(VALID_NAME);
        faculty.setId(VALID_ID);

        Mockito.when(facultyDao.getById(VALID_ID)).thenReturn(faculty);
        Assertions.assertEquals(faculty, facultyService.getById(VALID_ID));
    }

    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> facultyService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> facultyService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> facultyService.getById(null));
    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(facultyDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> facultyService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldReturnResultList_WhenConditionCompleted() {
        Faculty faculty = new Faculty();
        faculty.setName(VALID_NAME);
        faculty.setId(VALID_ID);

        List<Faculty> expectResult = new ArrayList<>();
        expectResult.add(faculty);

        Mockito.when(facultyDao.getAll(1, 10)).thenReturn(expectResult);
        Assertions.assertEquals(expectResult, facultyService.getAll(1, 10));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(facultyDao.getAll(10, 10)).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NotFoundException.class,
                () -> facultyService.getAll(10, 10));
    }


    @Test
    public void update_ShouldThrowException_WhenNameNotValid() {
        Faculty faculty = new Faculty();
        faculty.setId(BigInteger.ONE);

        faculty.setName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.update(faculty));

        faculty.setName("Notvalid ");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.update(faculty));

        faculty.setName("Not  valid");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName("Not+valid");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName("Not  valid vkvkvkvkvkvkvkvkvkvkv");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.insert(faculty));

        faculty.setName("n");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.update(faculty));

        faculty.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.update(faculty));

        faculty.setName("");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.update(faculty));

        faculty.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.update(faculty));

        faculty.setName("   ");
        Assertions.assertThrows(ValidationException.class, () -> facultyService.update(faculty));

        faculty.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> facultyService.update(faculty));
    }

    @Test
    public void update_ShouldThrowException_WhenIdNotValid() {
        Faculty faculty = new Faculty();
        faculty.setName(VALID_NAME);

        faculty.setId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> facultyService.update(faculty));

        faculty.setId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> facultyService.update(faculty));
    }

    @Test
    public void update_ShouldThrowException_WhenEntryNotExist() {

        Mockito.when(facultyDao.getById(BigInteger.ONE)).thenThrow(NotFoundException.class);

        Faculty faculty = new Faculty();
        faculty.setName(VALID_NAME);
        faculty.setId(VALID_ID);

        Assertions.assertThrows(ValidationException.class, () -> facultyService.update(faculty));
    }

    @Test
    public void update_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> facultyService.update(null));
    }


    @Test
    public void deleteById_ShouldThrowException_WhenIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> facultyService.deleteById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> facultyService.deleteById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> facultyService.deleteById(null));
    }

    @Test
    public void deleteById_ShouldThrowException_WhenEntryNotExist() {
        Mockito.when(facultyDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> facultyService.deleteById(VALID_ID));
    }
}
