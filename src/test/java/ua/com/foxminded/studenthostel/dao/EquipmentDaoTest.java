package ua.com.foxminded.studenthostel.dao;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Equipment;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class EquipmentDaoTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EquipmentDao equipmentDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static ResourceDatabasePopulator sqlScripts;

    @BeforeEach
    public void initScript() {
        sqlScripts = new ResourceDatabasePopulator();
    }

    @Test
    public void insert_ShouldMakeEntry_InEquipmentsTable() {
        Equipment equipment = new Equipment();
        equipment.setName("Table");

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");
        equipmentDao.insert(equipment);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void insert_ShouldReturnId_WhenEntryIsInserted() {
        Equipment equipment = new Equipment();
        equipment.setName("Table");

        Assertions.assertEquals(ONE, equipmentDao.insert(equipment));
    }

    @Test
    public void insert_ShouldThrowException_WhenNameNotUnique() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Equipment equipment = new Equipment();
        equipment.setName("Table");

        Assertions.assertThrows(DaoException.class, () -> equipmentDao.insert(equipment));
    }

    @Test
    public void assignToStudent_ShouldMakeEntry_InStudentsEquipmentsTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
        equipmentDao.assignToStudent(ONE, ONE);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void assignToStudent_ShouldThrowException_WhenStudentIdOrEquipmentIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(NotFoundException.class, () ->
                equipmentDao.assignToStudent(BigInteger.valueOf(7), BigInteger.valueOf(7)));

        Assertions.assertThrows(NotFoundException.class, () ->
                equipmentDao.assignToStudent(ONE, BigInteger.valueOf(7)));
    }

    @Test
    public void getById_ShouldReturnEquipment_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Equipment equipment = new Equipment();
        equipment.setId(ONE);
        equipment.setName("Table");

        Assertions.assertEquals(equipment, equipmentDao.getById(ONE));
    }

    @Test
    public void getById_ShouldThrowException_WhenEntryNotExist() {

        Assertions.assertThrows(NotFoundException.class,
                () -> equipmentDao.getById(ONE));
    }

    @Test
    public void getAll_ShouldReturnListOfEquipments_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Equipment equipment = new Equipment();
        equipment.setId(BigInteger.valueOf(5));
        equipment.setName("Mattress");

        List<Equipment> list = new ArrayList<>();
        list.add(equipment);

        Assertions.assertEquals(list, equipmentDao.getAll(4, 1));
    }

    @Test
    public void unassignFromStudent_ShouldReturnTrue_WhenEntryIsDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
         equipmentDao.unassignFromStudent(ONE, ONE);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore - 1, rowNumberAfter);
    }

    @Test
    public void unassignFromStudent_ShouldReturnFalse_WhenEntryIsNotDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
         equipmentDao.unassignFromStudent(BigInteger.valueOf(2), ONE);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore, rowNumberAfter);
    }
    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Equipment newValues = new Equipment();
        newValues.setId(ONE);
        newValues.setName("Newname");

         equipmentDao.update(newValues);

        Assertions.assertEquals(newValues, equipmentDao.getById(ONE));
    }

    @Test
    public void update_ShouldThrowException_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Equipment newValues = new Equipment();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("Newname");

        Assertions.assertThrows(DaoException.class, () -> equipmentDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");
        equipmentDao.deleteById(BigInteger.valueOf(6));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }
}
