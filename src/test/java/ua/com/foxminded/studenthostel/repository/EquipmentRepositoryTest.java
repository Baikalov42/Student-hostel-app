package ua.com.foxminded.studenthostel.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Equipment;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EquipmentRepositoryTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private EquipmentRepository repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void insert_ShouldMakeEntry_InEquipmentsTable() {
        Equipment equipment = new Equipment();
        equipment.setName("Table");

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");
        repository.saveAndFlush(equipment);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void insert_ShouldReturnId_WhenEntryIsInserted() {
        Equipment equipment = new Equipment();
        equipment.setName("Table");

        Assertions.assertEquals(ONE, repository.saveAndFlush(equipment).getId());
    }

    @Test
    @Sql("/sql/AddDataToEquipmentsTable.sql")
    public void insert_ShouldThrowException_WhenNameNotUnique() {

        Equipment equipment = new Equipment();
        equipment.setName("Table");

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(equipment));
    }

    @Test
    @Sql(scripts = {
            "/sql/AddDataToEquipmentsTable.sql",
            "/sql/AddDataToStudentsTable.sql"})
    public void assignToStudent_ShouldMakeEntry_InStudentsEquipmentsTable() {

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
        repository.assignToStudent(ONE, ONE);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    @Sql(scripts = {
            "/sql/AddDataToEquipmentsTable.sql",
            "/sql/AddDataToStudentsTable.sql"})
    public void assignToStudent_ShouldThrowException_WhenStudentIdOrEquipmentIdNotExist() {

        Assertions.assertThrows(NotFoundException.class, () ->
                repository.assignToStudent(BigInteger.valueOf(7), BigInteger.valueOf(7)));

        Assertions.assertThrows(NotFoundException.class, () ->
                repository.assignToStudent(ONE, BigInteger.valueOf(7)));
    }

    @Test
    @Sql("/sql/AddDataToEquipmentsTable.sql")
    public void getById_ShouldReturnEquipment_WhenEntryIsExist() {

        Equipment equipment = new Equipment();
        equipment.setId(ONE);
        equipment.setName("Table");

        Assertions.assertEquals(equipment, repository.findById(ONE).get());
    }

    @Test
    @Sql("/sql/AddDataToEquipmentsTable.sql")
    public void getAll_ShouldReturnListOfEquipments_WhenConditionCompleted() {

        Equipment equipment = new Equipment();
        equipment.setId(BigInteger.valueOf(5));
        equipment.setName("Mattress");

        List<Equipment> list = new ArrayList<>();
        list.add(equipment);

        Pageable pageable = PageRequest.of(4, 1, Sort.Direction.ASC, "id");
        Assertions.assertEquals(list, repository.findAll(pageable).getContent());
    }


    @Test
    @Sql(scripts = {
            "/sql/AddDataToEquipmentsTable.sql",
            "/sql/AddDataToStudentsTable.sql",
            "/sql/AddDataToStudentsEquipmentsTable.sql"})
    public void unassignFromStudent_ShouldReturnTrue_WhenEntryIsDeleted() {

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
        repository.unassignFromStudent(ONE, ONE);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore - 1, rowNumberAfter);
    }

    @Test
    @Sql(scripts = {
            "/sql/AddDataToEquipmentsTable.sql",
            "/sql/AddDataToStudentsTable.sql",
            "/sql/AddDataToStudentsEquipmentsTable.sql"})
    public void unassignFromStudent_ShouldReturnFalse_WhenEntryIsNotDeleted() {

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
        repository.unassignFromStudent(BigInteger.valueOf(2), ONE);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore, rowNumberAfter);
    }

    @Test
    @Sql("/sql/AddDataToEquipmentsTable.sql")
    public void update_ShouldUpdateEntry_WhenDataExist() {

        Equipment newValues = new Equipment();
        newValues.setId(ONE);
        newValues.setName("Newname");

        repository.saveAndFlush(newValues);

        Assertions.assertEquals(newValues, repository.findById(ONE).get());
    }

    @Test
    @Sql("/sql/AddDataToEquipmentsTable.sql")
    public void update_ShouldThrowException_WhenDataNotExist() {

        Equipment newValues = new Equipment();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("Newname");

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(newValues));
    }

    @Test
    @Sql("/sql/AddDataToEquipmentsTable.sql")
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");
        repository.deleteById(BigInteger.valueOf(6));
        repository.flush();
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }
}
