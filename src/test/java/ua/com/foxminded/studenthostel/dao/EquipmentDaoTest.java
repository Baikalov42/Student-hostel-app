package ua.com.foxminded.studenthostel.dao;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.models.Equipment;

import javax.sql.DataSource;
import java.math.BigInteger;

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
    public void save_ShouldMakeEntry_InEquipmentsTable() {
        Equipment equipment = Equipment.TABLE;
        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");
        equipmentDao.save(equipment);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "equipments");
        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);

    }

    @Test
    public void setToStudent_ShouldMakeEntry_InStudentsEquipmentsTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
        equipmentDao.setToStudent(BigInteger.valueOf(1), BigInteger.valueOf(1));
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void setToStudent_ShouldThrowException_WhenStudentIdOrEquipmentIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                equipmentDao.setToStudent(BigInteger.valueOf(7), BigInteger.valueOf(7)));

        Assertions.assertThrows(DataIntegrityViolationException.class, () ->
                equipmentDao.setToStudent(BigInteger.valueOf(1), BigInteger.valueOf(7)));

    }


    @Test
    public void getById_ShouldReturnEquipment_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Equipment equipment = Equipment.TABLE;
        Assertions.assertEquals(equipment, equipmentDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getById_ShouldThrowException_WhenEntryNotExist() {

        Assertions.assertThrows(EmptyResultDataAccessException.class,
                () -> equipmentDao.getById(BigInteger.valueOf(1)));
    }
    @Test
    public void removeFromStudent_ShouldReturnTrue_WhenEntryIsDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
        boolean isRemoved = equipmentDao.removeFromStudent(BigInteger.valueOf(1), BigInteger.valueOf(1));
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore - 1, rowNumberAfter);
        Assertions.assertTrue(isRemoved);
    }
    @Test
    public void removeFromStudent_ShouldReturnFalse_WhenEntryIsNotDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToEquipmentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsEquipmentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");
        boolean isRemoved = equipmentDao.removeFromStudent(BigInteger.valueOf(2), BigInteger.valueOf(1));
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students_equipments");

        Assertions.assertEquals(rowNumberBefore , rowNumberAfter);
        Assertions.assertFalse(isRemoved);
    }
}
