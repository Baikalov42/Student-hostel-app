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
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.Student;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class StudentDaoTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @PersistenceContext
    private EntityManager entityManager;

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
    }

    @Test
    public void insert_ShouldMakeEntry_InStudentsTable() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Student student = getStudent();

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        studentDao.insert(student);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");

        Assertions.assertEquals(rowBefore + 1, rowAfter);
    }

    @Test
    public void insert_ShouldThrowException_WhenGroupIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group group = new Group();
        group.setId(BigInteger.TEN);

        Student student = getStudent();
        student.setGroup(group);

        Assertions.assertThrows(DaoException.class, () -> studentDao.insert(student));
    }

    @Test
    public void insert_ShouldThrowException_WhenRoomIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room room = new Room();
        room.setId(BigInteger.TEN);

        Student student = getStudent();
        student.setRoom(room);

        Assertions.assertThrows(DaoException.class, () -> studentDao.insert(student));
    }

    @Test
    public void getById_ShouldReturnStudent_WhenStudentIdIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Student student = getStudent();

        Assertions.assertEquals(student, studentDao.getById(ONE));
    }

    @Test
    public void getById_ShouldThrowException_WhenStudentIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(NotFoundException.class,
                () -> studentDao.getById(BigInteger.valueOf(10)));
    }

    @Test
    public void getAll_ShouldReturnListOfStudents_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        List<Student> list = new ArrayList<>();
        list.add(getStudent());

        Assertions.assertEquals(list, studentDao.getAll(0, 1));
    }

    @Test
    public void getAllByFloor_ShouldReturnListOfStudents_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Group group = entityManager.find(Group.class, BigInteger.valueOf(2));

        List<Student> expect = new ArrayList<>();
        expect.add(new Student(ONE, "Namefirst", "Lastfirst",
                10, getGroup(), getRoom()));

        expect.add(new Student(BigInteger.valueOf(2), "Namesec", "Lastsec",
                0, group, getRoom()));

        expect.add(new Student(BigInteger.valueOf(3), "Namethree", "Lastthree",
                10, getGroup(), getRoom()));

        Assertions.assertEquals(expect, studentDao.getAllByFloor(ONE));
    }

    @Test
    public void showByFaculty_ShouldReturnListOfStudents_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room room = entityManager.find(Room.class, BigInteger.valueOf(2));

        List<Student> expect = new ArrayList<>();
        expect.add(new Student(ONE, "Namefirst", "Lastfirst",
                10, getGroup(), getRoom()));

        expect.add(new Student(BigInteger.valueOf(3), "Namethree", "Lastthree",
                10, getGroup(), getRoom()));

        expect.add(new Student(BigInteger.valueOf(5), "Namefive", "Lastfive",
                6, getGroup(), room));

        Assertions.assertEquals(expect, studentDao.getAllByFaculty(ONE));

    }

    @Test
    public void getAllByCourse_ShouldReturnListOfStudents_WhenConditionCompleted() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Room room = entityManager.find(Room.class, BigInteger.valueOf(2));

        List<Student> expect = new ArrayList<>();
        expect.add(new Student(ONE, "Namefirst", "Lastfirst",
                10, getGroup(), getRoom()));

        expect.add(new Student(BigInteger.valueOf(3), "Namethree", "Lastthree",
                10, getGroup(), getRoom()));

        expect.add(new Student(BigInteger.valueOf(5), "Namefive", "Lastfive",
                6, getGroup(), room));

        Assertions.assertEquals(expect, studentDao.getAllByCourse(ONE));

    }


    @Test
    public void changeRoom_ShouldThrowException_WhenNewRoomIdNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Assertions.assertThrows(NotFoundException.class,
                () -> studentDao.changeRoom(BigInteger.valueOf(10), ONE));
    }

    @Test
    public void changeDebt_ShouldUpdateEntry_WhenDataIsCorrect() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToRoomsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int newHoursDebt = 15;
        Student expect = studentDao.getById(ONE);
        expect.setHoursDebt(15);

        studentDao.changeDebt(newHoursDebt, expect.getId());
        Student actual = studentDao.getById(ONE);

        Assertions.assertEquals(expect, actual);
    }

    @Test
    public void update_ShouldUpdateEntry_WhenDataExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);


        Student newValues = new Student(ONE, "Newfirstname", "Newlastname",
                10, getGroup(), getRoom());

        studentDao.update(newValues);
        Assertions.assertEquals(newValues, studentDao.getById(ONE));
    }

    @Test
    public void update_ShouldThrowException_WhenDataNotExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        Student newValues = new Student(BigInteger.valueOf(7), "Newfirstname", "Newlastname",
                10, getGroup(), getRoom());

        Assertions.assertThrows(DaoException.class, () -> studentDao.update(newValues));
    }

    @Test
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {
        sqlScripts.addScript(new ClassPathResource("sql\\AddDataToStudentsTable.sql"));
        DatabasePopulatorUtils.execute(sqlScripts, dataSource);

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        studentDao.deleteById(ONE);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }

    Student getStudent() {
        Student student = new Student();
        student.setId(ONE);
        student.setFirstName("Namefirst");
        student.setLastName("Lastfirst");
        student.setHoursDebt(10);
        student.setRoom(getRoom());
        student.setGroup(getGroup());

        return student;
    }

    Room getRoom() {
        Room room = new Room();
        room.setName("RM-0001");
        room.setFloor(getfloor());
        room.setId(ONE);

        return room;
    }

    Group getGroup() {
        Group group = new Group();
        group.setId(ONE);
        group.setCourseNumber(getCourse());
        group.setFaculty(getFaculty());
        return group;
    }

    CourseNumber getCourse() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(ONE);
        courseNumber.setName("Courseone");

        return courseNumber;
    }
    Faculty getFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(ONE);
        faculty.setName("Facultyone");

        return faculty;
    }
    Floor getfloor() {
        Floor floor = new Floor();
        floor.setId(ONE);
        floor.setName("Firstfloor");

        return floor;
    }
}
