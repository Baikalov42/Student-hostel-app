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
import ua.com.foxminded.studenthostel.repository.StudentRepository;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Equipment;

import java.math.BigInteger;
import java.util.Optional;

@SpringBootTest
class EquipmentServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "Name name";
    public static final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

    @Autowired
    @InjectMocks
    private EquipmentService equipmentService;

    @Autowired
    @InjectMocks
    private StudentService studentService;

    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private StudentRepository studentRepository;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Equipment toDB = new Equipment();
        Equipment fromDB = getEquipment(VALID_ID, VALID_NAME);

        Mockito.when(equipmentRepository.save(toDB)).thenReturn(fromDB);

        toDB.setName("Name Name");
        Assertions.assertEquals(VALID_ID, equipmentService.insert(toDB));

        toDB.setName("Name NAME");
        Assertions.assertEquals(VALID_ID, equipmentService.insert(toDB));

        toDB.setName("NAme NAmE123");
        Assertions.assertEquals(VALID_ID, equipmentService.insert(toDB));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenNameNotValid() {
        Equipment equipment = new Equipment();

        equipment.setName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("Notvalid ");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("Not  valid");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("Not+valid");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("Not  valid vkvkvkvkvkvkvkvkvkvkv");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("n");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("N");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("   ");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));
    }

    @Test
    public void insert_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.insert(null));
    }


    @Test
    public void assignToStudent_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.assignToStudent(ZERO_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.assignToStudent(NEGATIVE_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.assignToStudent(VALID_ID, ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.assignToStudent(VALID_ID, NEGATIVE_ID));
    }

    @Test
    public void assignToStudent_ShouldThrowException_WhenStudentNotExist() {
        Mockito.when(studentRepository.existsById(VALID_ID)).thenReturn(false);
        Mockito.when(equipmentRepository.existsById(VALID_ID)).thenReturn(true);

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.assignToStudent(VALID_ID, VALID_ID));
    }

    @Test
    public void assignToStudent_ShouldThrowException_WhenEquipmentNotExist() {
        Mockito.when(studentRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(equipmentRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.assignToStudent(VALID_ID, VALID_ID));
    }


    @Test
    public void unassignFromStudent_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.unassignFromStudent(ZERO_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.unassignFromStudent(NEGATIVE_ID, VALID_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.unassignFromStudent(VALID_ID, ZERO_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.unassignFromStudent(VALID_ID, NEGATIVE_ID));
    }

    @Test
    public void unassignFromStudent_ShouldThrowException_WhenStudentNotExist() {
        Mockito.when(studentRepository.existsById(VALID_ID)).thenReturn(false);
        Mockito.when(equipmentRepository.existsById(VALID_ID)).thenReturn(true);

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.unassignFromStudent(VALID_ID, VALID_ID));
    }

    @Test
    public void unassignFromStudent_ShouldThrowException_WhenEquipmentNotExist() {
        Mockito.when(studentRepository.existsById(VALID_ID)).thenReturn(true);
        Mockito.when(equipmentRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.unassignFromStudent(VALID_ID, VALID_ID));
    }

    @Test
    public void getById_ShouldReturnEquipment_WhenIdIsValid() {
        Equipment equipment = getEquipment(VALID_ID, VALID_NAME);

        Mockito.when(equipmentRepository.findById(VALID_ID)).thenReturn(Optional.of(equipment));
        Assertions.assertEquals(equipment, equipmentService.getById(VALID_ID));
    }


    @Test
    public void getById_ShouldThrowException_WhenIdNotValid() {

        Assertions.assertThrows(ValidationException.class, () -> equipmentService.getById(ZERO_ID));

        Assertions.assertThrows(ValidationException.class, () -> equipmentService.getById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class, () -> equipmentService.getById(null));
    }

    @Test
    public void getById_ShouldThrowException_WhenResultIsEmpty() {

        Mockito.when(equipmentRepository.findById(VALID_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> equipmentService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(equipmentRepository.findAll(PAGEABLE)).thenReturn(Page.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> equipmentService.getAll(0));
    }

    @Test
    public void update_ShouldThrowException_WhenNameNotValid() {
        Equipment equipment = new Equipment();
        equipment.setId(BigInteger.ONE);

        equipment.setName("notvalid");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));

        equipment.setName("Notvalid ");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));

        equipment.setName("Not  valid");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("Not+valid");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("Not  valid vkvkvkvkvkvkvkvkvkvkv");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.insert(equipment));

        equipment.setName("n");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));

        equipment.setName("123");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));

        equipment.setName("");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));

        equipment.setName(" ");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));

        equipment.setName("   ");
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));

        equipment.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));
    }

    @Test
    public void update_ShouldThrowException_WhenIdNotValid() {
        Equipment equipment = new Equipment();
        equipment.setName(VALID_NAME);

        equipment.setId(ZERO_ID);
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));

        equipment.setId(NEGATIVE_ID);
        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));
    }

    @Test
    public void update_ShouldThrowException_WhenEntryNotExist() {

        Mockito.when(equipmentRepository.existsById(VALID_ID)).thenReturn(false);

        Equipment equipment = getEquipment(VALID_ID, VALID_NAME);

        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));
    }

    @Test
    public void update_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.update(null));
    }


    @Test
    public void deleteById_ShouldThrowException_WhenIdNotValid() {
        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.deleteById(NEGATIVE_ID));

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.deleteById(ZERO_ID));


        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.deleteById(null));
    }

    @Test
    public void deleteById_ShouldThrowException_WhenEntryNotExist() {
        Mockito.when(equipmentRepository.existsById(VALID_ID)).thenReturn(false);

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.deleteById(VALID_ID));
    }

    private Equipment getEquipment(BigInteger id, String name) {

        Equipment equipment = new Equipment();
        equipment.setId(id);
        equipment.setName(name);
        return equipment;
    }
}
