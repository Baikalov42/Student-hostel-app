package ua.com.foxminded.studenthostel.dao;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Equipment;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class EquipmentDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    EquipmentDao equipmentDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static ResourceDatabasePopulator sqlScripts;

    @BeforeEach
    public void addTablesScript() {
        sqlScripts = new ResourceDatabasePopulator();
        sqlScripts.addScript(new ClassPathResource("sql\\CreateTables.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);
    }

    @Test
    public void insert_ShouldMakeEntry_InEquipmentsTable() {
        Equipment equipment = new Equipment();
        equipment.setName("table");

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");
        equipmentDao.insert(equipment);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void insert_ShouldReturnId_WhenEntryIsInserted() {
        Equipment equipment = new Equipment();
        equipment.setName("table");
        Assertions.assertEquals(BigInteger.valueOf(1), equipmentDao.insert(equipment));
    }

    @Test
    public void insert_ShouldThrowException_WhenNameNotUnique() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Equipment equipment = new Equipment();
        equipment.setName("table");

        Assertions.assertThrows(DuplicateKeyException.class, () -> equipmentDao.insert(equipment));
    }

    @Test
    public void assignToStudent_ShouldMakeEntry_InStudentsEquipmentsTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
        boolean isAssign = equipmentDao.assignToStudent(BigInteger.valueOf(1), BigInteger.valueOf(1));
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
        Assertions.assertTrue(isAssign);
    }

    @Test
    public void assignToStudent_ShouldThrowException_WhenStudentIdOrEquipmentIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                equipmentDao.assignToStudent(BigInteger.valueOf(7), BigInteger.valueOf(7)));

        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                equipmentDao.assignToStudent(BigInteger.valueOf(1), BigInteger.valueOf(7)));
    }

    @Test
    public void getById_ShouldReturnEquipment_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Equipment equipment = new Equipment();
        equipment.setId(BigInteger.valueOf(1));
        equipment.setName("table");

        Assertions.assertEquals(equipment, equipmentDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getById_ShouldThrowException_WhenEntryNotExist() {

        Assertions.assertThrows(DaoException.class,
                () -> equipmentDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getAll_ShouldReturnListOfEquipments_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Equipment equipment = new Equipment();
        equipment.setId(BigInteger.valueOf(5));
        equipment.setName("mattress");

        List<Equipment> list = new ArrayList<>();
        list.add(equipment);

        Assertions.assertEquals(list, equipmentDao.getAll(1, 4));
    }

    @Test
    public void unassignFromStudent_ShouldReturnTrue_WhenEntryIsDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
        boolean isRemoved = equipmentDao.unassignFromStudent(BigInteger.valueOf(1), BigInteger.valueOf(1));
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore - 1, rowNumberAfter);
        Assertions.assertTrue(isRemoved);
    }

    @Test
    public void unassignFromStudent_ShouldReturnFalse_WhenEntryIsNotDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
        boolean isRemoved = equipmentDao.unassignFromStudent(BigInteger.valueOf(2), BigInteger.valueOf(1));
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore, rowNumberAfter);
        Assertions.assertFalse(isRemoved);
    }

    @Test
    public void deleteById_ShouldReturnTrue_WhenEntryIsDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");
        boolean isDeleted = equipmentDao.deleteById(BigInteger.valueOf(6));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
        Assertions.assertTrue(isDeleted);
    }

    @Test
    public void deleteById_ShouldReturnFalse_WhenEntryNotDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");
        boolean isDeleted = equipmentDao.deleteById(BigInteger.valueOf(7));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");

        Assertions.assertEquals(rowBefore, rowAfter);
        Assertions.assertFalse(isDeleted);
    }
}
