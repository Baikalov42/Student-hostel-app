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
import ua.com.foxminded.studenthostel.dao.StudentDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.Student;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class EquipmentServiceTest {

    public static final BigInteger VALID_ID = BigInteger.ONE;
    public static final BigInteger NEGATIVE_ID = BigInteger.valueOf(-1);
    public static final BigInteger ZERO_ID = BigInteger.ZERO;
    public static final String VALID_NAME = "Name name";

    @Autowired
    @InjectMocks
    private EquipmentService equipmentService;

    @Autowired
    @InjectMocks
    private StudentService studentService;

    @Mock
    private EquipmentDao equipmentDao;
    @Mock
    private StudentDao studentDao;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insert_ShouldReturnId_WhenInputIsValid() {
        Equipment equipment = new Equipment();
        Mockito.when(equipmentDao.insert(equipment)).thenReturn(VALID_ID);

        equipment.setName("Name Name");
        Assertions.assertEquals(VALID_ID, equipmentService.insert(equipment));

        equipment.setName("Name NAME");
        Assertions.assertEquals(VALID_ID, equipmentService.insert(equipment));

        equipment.setName("NAme NAmE123");
        Assertions.assertEquals(VALID_ID, equipmentService.insert(equipment));
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
    public void assignToStudent_ShouldReturnTrue_WhenEquipmentAssignedToStudent() {
        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(equipmentDao.getById(VALID_ID)).thenReturn(new Equipment());

        Mockito.when(equipmentDao.assignToStudent(VALID_ID, VALID_ID)).thenReturn(true);

        Assertions.assertTrue(equipmentService.assignToStudent(VALID_ID, VALID_ID));
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
        Mockito.when(studentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Mockito.when(equipmentDao.getById(VALID_ID)).thenReturn(new Equipment());

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.assignToStudent(VALID_ID, VALID_ID));
    }

    @Test
    public void assignToStudent_ShouldThrowException_WhenEquipmentNotExist() {
        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(equipmentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.assignToStudent(VALID_ID, VALID_ID));
    }

    @Test
    public void unassignFromStudent_ShouldReturnTrue_WhenEquipmentUnassignedFromStudent() {
        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(equipmentDao.getById(VALID_ID)).thenReturn(new Equipment());

        Mockito.when(equipmentDao.unassignFromStudent(VALID_ID, VALID_ID)).thenReturn(true);

        Assertions.assertTrue(equipmentService.unassignFromStudent(VALID_ID, VALID_ID));
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
        Mockito.when(studentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);
        Mockito.when(equipmentDao.getById(VALID_ID)).thenReturn(new Equipment());

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.unassignFromStudent(VALID_ID, VALID_ID));
    }

    @Test
    public void unassignFromStudent_ShouldThrowException_WhenEquipmentNotExist() {
        Mockito.when(studentDao.getById(VALID_ID)).thenReturn(new Student());
        Mockito.when(equipmentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.unassignFromStudent(VALID_ID, VALID_ID));
    }

    @Test
    public void getById_ShouldReturnEquipment_WhenIdIsValid() {
        Equipment equipment = new Equipment();
        equipment.setId(VALID_ID);
        equipment.setName(VALID_NAME);

        Mockito.when(equipmentDao.getById(VALID_ID)).thenReturn(equipment);
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

        Mockito.when(equipmentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(NotFoundException.class,
                () -> equipmentService.getById(VALID_ID));
    }

    @Test
    public void getAll_ShouldReturnResultList_WhenConditionCompleted() {
        Equipment equipment = new Equipment();
        equipment.setId(VALID_ID);
        equipment.setName(VALID_NAME);

        List<Equipment> expectResult = new ArrayList<>();
        expectResult.add(equipment);

        Mockito.when(equipmentDao.getAll(1, 10)).thenReturn(expectResult);
        Assertions.assertEquals(expectResult, equipmentService.getAll(1, 10));
    }

    @Test
    public void getAll_ShouldThrowException_WhenResultIsEmpty() {
        Mockito.when(equipmentDao.getAll(10, 10)).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NotFoundException.class,
                () -> equipmentService.getAll(10, 10));
    }

    @Test
    public void update_ShouldReturnTrue_WhenEntryIsUpdated() {
        Equipment equipment = new Equipment();
        equipment.setId(VALID_ID);
        equipment.setName(VALID_NAME);

        Mockito.when(equipmentDao.update(equipment)).thenReturn(true);

        Assertions.assertTrue(equipmentService.update(equipment));
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

        Mockito.when(equipmentService.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Equipment equipment = new Equipment();
        equipment.setName(VALID_NAME);
        equipment.setId(VALID_ID);

        Assertions.assertThrows(ValidationException.class, () -> equipmentService.update(equipment));
    }

    @Test
    public void update_ShouldThrowExceptionWhenObjectIsNull() {
        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.update(null));
    }

    @Test
    public void deleteById_ShouldReturnTrue_WhenEntryIsDeleted() {
        Mockito.when(equipmentDao.deleteById(VALID_ID)).thenReturn(true);
        Mockito.when(equipmentDao.getById(VALID_ID)).thenReturn(new Equipment());

        Assertions.assertTrue(equipmentService.deleteById(VALID_ID));
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
        Mockito.when(equipmentDao.getById(VALID_ID)).thenThrow(NotFoundException.class);

        Assertions.assertThrows(ValidationException.class,
                () -> equipmentService.deleteById(VALID_ID));
    }
}
