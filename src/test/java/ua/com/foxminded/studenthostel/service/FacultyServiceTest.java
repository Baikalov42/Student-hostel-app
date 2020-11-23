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
import ua.com.foxminded.studenthostel.repository.FacultyRepository;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Faculty;

import java.math.BigInteger;
import java.util.Optional;

@SpringBootTest
class FacultyServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "Name name";
    public static final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

    @Autowired
    @InjectMocks
    private FacultyService facultyService;

    @Mock
    private FacultyRepository facultyRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Faculty toDB = new Faculty();
        Faculty fromDB = getFaculty(VALID_ID, VALID_NAME);

        Mockito.when(facultyRepository.save(toDB)).thenReturn(fromDB);

        toDB.setName("Name Name");
        Assertions.assertEquals(VALID_ID, facultyService.insert(toDB));

        toDB.setName("Name NAME");
        Assertions.assertEquals(VALID_ID, facultyService.insert(toDB));

        toDB.setName("NAme NAmE123");
        Assertions.assertEquals(VALID_ID, facultyService.insert(toDB));
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
        Faculty faculty = getFaculty(VALID_ID, VALID_NAME);

        Mockito.when(facultyRepository.findById(VALID_ID)).thenReturn(Optional.of(faculty));
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

        Mockito.when(facultyRepository.findById(VALID_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> facultyService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(facultyRepository.findAll(PAGEABLE)).thenReturn(Page.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> facultyService.getAll(0));
    }

    @Test
    public void update_ShouldThrowException_WhenNameNotValid() {
        Faculty faculty = new Faculty();
        faculty.setId(VALID_ID);

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

        Mockito.when(facultyRepository.existsById(VALID_ID)).thenReturn(false);
        Faculty faculty = getFaculty(VALID_ID, VALID_NAME);

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
        Mockito.when(facultyRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> facultyService.deleteById(VALID_ID));
    }

    private Faculty getFaculty(BigInteger id, String name) {
        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        return faculty;
    }
}
