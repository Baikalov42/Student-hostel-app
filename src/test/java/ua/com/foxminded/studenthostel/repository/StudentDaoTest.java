package ua.com.foxminded.studenthostel.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.Student;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StudentDaoTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void insert_ShouldMakeEntry_InStudentsTable() {

        Student student = getStudent();

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        studentRepository.saveAndFlush(student);
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");

        Assertions.assertEquals(rowBefore + 1, rowAfter);
    }

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void insert_ShouldThrowException_WhenGroupIdNotExist() {

        Group group = new Group();
        group.setId(BigInteger.TEN);

        Student student = getStudent();
        student.setGroup(group);

        Assertions.assertThrows(DataAccessException.class,
                () -> studentRepository.saveAndFlush(student));
    }

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void insert_ShouldThrowException_WhenRoomIdNotExist() {

        Room room = new Room();
        room.setId(BigInteger.TEN);

        Student student = getStudent();
        student.setRoom(room);

        Assertions.assertThrows(DataAccessException.class,
                () -> studentRepository.saveAndFlush(student));
    }

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void getById_ShouldReturnStudent_WhenStudentIdIsExist() {

        Student student = getStudent();
        Assertions.assertEquals(student, studentRepository.findById(ONE).get());
    }

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void getAll_ShouldReturnListOfStudents_WhenConditionCompleted() {

        List<Student> list = new ArrayList<>();
        list.add(getStudent());

        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.ASC, "id");
        Assertions.assertEquals(list, studentRepository.findAll(pageable).getContent());
    }

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void getAllByFloor_ShouldReturnListOfStudents_WhenConditionCompleted() {

        Group group = groupRepository.findById(BigInteger.valueOf(2)).get();

        List<Student> expect = new ArrayList<>();
        expect.add(new Student(ONE, "Namefirst", "Lastfirst",
                10, getGroup(), getRoom()));

        expect.add(new Student(BigInteger.valueOf(2), "Namesec", "Lastsec",
                0, group, getRoom()));

        expect.add(new Student(BigInteger.valueOf(3), "Namethree", "Lastthree",
                10, getGroup(), getRoom()));

        Assertions.assertEquals(expect, studentRepository.getAllByFloor(ONE));
    }

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void showByFaculty_ShouldReturnListOfStudents_WhenConditionCompleted() {

        Room room = roomRepository.findById(BigInteger.valueOf(2)).get();

        List<Student> expect = new ArrayList<>();
        expect.add(new Student(ONE, "Namefirst", "Lastfirst",
                10, getGroup(), getRoom()));

        expect.add(new Student(BigInteger.valueOf(3), "Namethree", "Lastthree",
                10, getGroup(), getRoom()));

        expect.add(new Student(BigInteger.valueOf(5), "Namefive", "Lastfive",
                6, getGroup(), room));

        Assertions.assertEquals(expect, studentRepository.getAllByFaculty(ONE));

    }

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void getAllByCourse_ShouldReturnListOfStudents_WhenConditionCompleted() {

        Room room = roomRepository.findById(BigInteger.valueOf(2)).get();

        List<Student> expect = new ArrayList<>();
        expect.add(new Student(ONE, "Namefirst", "Lastfirst",
                10, getGroup(), getRoom()));

        expect.add(new Student(BigInteger.valueOf(3), "Namethree", "Lastthree",
                10, getGroup(), getRoom()));

        expect.add(new Student(BigInteger.valueOf(5), "Namefive", "Lastfive",
                6, getGroup(), room));

        Assertions.assertEquals(expect, studentRepository.getAllByCourse(ONE));

    }


    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void changeRoom_ShouldThrowException_WhenNewRoomIdNotExist() {

        Assertions.assertThrows(NotFoundException.class,
                () -> studentRepository.changeRoom(BigInteger.valueOf(10), ONE));
    }

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void changeDebt_ShouldUpdateEntry_WhenDataIsCorrect() {

        int newHoursDebt = 15;
        Student expect = studentRepository.findById(ONE).get();
        expect.setHoursDebt(15);

        studentRepository.changeDebt(newHoursDebt, expect.getId());
        Student actual = studentRepository.findById(ONE).get();

        Assertions.assertEquals(expect, actual);
    }

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void update_ShouldUpdateEntry_WhenDataExist() {

        Student newValues = new Student(ONE, "Newfirstname", "Newlastname",
                10, getGroup(), getRoom());

        studentRepository.saveAndFlush(newValues);
        Assertions.assertEquals(newValues, studentRepository.findById(ONE).get());
    }

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void update_ShouldThrowException_WhenDataNotExist() {

        Student newValues = new Student(BigInteger.valueOf(7), "Newfirstname", "Newlastname",
                10, getGroup(), getRoom());

        Assertions.assertThrows(DataAccessException.class,
                () -> studentRepository.saveAndFlush(newValues));
    }

    @Test
    @Sql("/sql/AddDataToStudentsTable.sql")
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "students");
        studentRepository.deleteById(ONE);
        studentRepository.flush();
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
