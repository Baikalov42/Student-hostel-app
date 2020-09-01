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
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Student;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringJUnitConfig(SpringConfig.class)
class StudentDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    StudentDao studentDao;

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
    public void insert_ShouldMakeEntry_InStudentsTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Student student = new Student(BigInteger.valueOf(10), "firstnameone", "lastnameone",
                10, BigInteger.valueOf(1), BigInteger.valueOf(1));


        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        studentDao.insert(student);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");

        Assertions.assertEquals(rowBefore + 1, rowAfter);
    }

    @Test
    public void insert_ShouldThrowException_WhenGroupIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Student student = new Student(BigInteger.valueOf(10), "firstnameone", "lastnameone",
                10, BigInteger.valueOf(5), BigInteger.valueOf(1));

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> studentDao.insert(student));
    }

    @Test
    public void insert_ShouldThrowException_WhenRoomIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Student student = new Student(BigInteger.valueOf(10), "firstnameone", "lastnameone",
                10, BigInteger.valueOf(1), BigInteger.valueOf(5));

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> studentDao.insert(student));
    }

    @Test
    public void getById_ShouldReturnStudent_WhenStudentIdIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Student student = new Student(BigInteger.valueOf(1), "firstnameone", "lastnameone",
                10, BigInteger.valueOf(1), BigInteger.valueOf(1));

        Assertions.assertEquals(student, studentDao.getById(BigInteger.valueOf(1)));
    }

    @Test
    public void getById_ShouldThrowException_WhenStudentIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(DaoException.class,
                () -> studentDao.getById(BigInteger.valueOf(10)));
    }
    @Test
    public void getAll_ShouldReturnListOfStudents() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        List<Student> list = new ArrayList<>();
        list.add(new Student(BigInteger.valueOf(3),"firstnamethree", "lastnamethree",
                10, BigInteger.valueOf(1), BigInteger.valueOf(1)));

        Assertions.assertEquals(list, studentDao.getAll(1,2));
    }

    @Test
    public void getAllByFloor_ShouldReturnListOfStudents_WhenDataIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        List<Student> expect = new ArrayList<>();
        expect.add(new Student(BigInteger.valueOf(1), "firstnameone", "lastnameone",
                10, BigInteger.valueOf(1), BigInteger.valueOf(1)));

        expect.add(new Student(BigInteger.valueOf(2), "firstnametwo", "lastnametwo",
                0, BigInteger.valueOf(2), BigInteger.valueOf(1)));

        expect.add(new Student(BigInteger.valueOf(3), "firstnamethree", "lastnamethree",
                10, BigInteger.valueOf(1), BigInteger.valueOf(1)));

        Assertions.assertEquals(expect, studentDao.getAllByFloor(BigInteger.valueOf(1)));
    }

    @Test
    public void showByFaculty_ShouldReturnListOfStudents_WhenDataIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        List<Student> expect = new ArrayList<>();
        expect.add(new Student(BigInteger.valueOf(1), "firstnameone", "lastnameone",
                10, BigInteger.valueOf(1), BigInteger.valueOf(1)));

        expect.add(new Student(BigInteger.valueOf(3), "firstnamethree", "lastnamethree",
                10, BigInteger.valueOf(1), BigInteger.valueOf(1)));

        expect.add(new Student(BigInteger.valueOf(5), "firstnamefive", "lastnamefive",
                6, BigInteger.valueOf(1), BigInteger.valueOf(2)));

        Assertions.assertEquals(expect, studentDao.getAllByFaculty(BigInteger.valueOf(1)));

    }

    @Test
    public void getAllByCourse_ShouldReturnListOfStudents_WhenDataIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        List<Student> expect = new ArrayList<>();
        expect.add(new Student(BigInteger.valueOf(1), "firstnameone", "lastnameone",
                10, BigInteger.valueOf(1), BigInteger.valueOf(1)));

        expect.add(new Student(BigInteger.valueOf(3), "firstnamethree", "lastnamethree",
                10, BigInteger.valueOf(1), BigInteger.valueOf(1)));

        expect.add(new Student(BigInteger.valueOf(5), "firstnamefive", "lastnamefive",
                6, BigInteger.valueOf(1), BigInteger.valueOf(2)));

        Assertions.assertEquals(expect, studentDao.getAllByCourse(BigInteger.valueOf(1)));

    }

    @Test
    public void deleteById_ShouldReturnTrue_WhenEntryIsDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        boolean isDeleted = studentDao.deleteById(BigInteger.valueOf(1));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
        Assertions.assertTrue(isDeleted);
    }

    @Test
    public void deleteById_ShouldReturnFalse_WhenEntryNotDeleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        boolean isDeleted = studentDao.deleteById(BigInteger.valueOf(7));
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");

        Assertions.assertEquals(rowBefore, rowAfter);
        Assertions.assertFalse(isDeleted);
    }
}
