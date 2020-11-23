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
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.Group;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GroupDaoTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private GroupRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql("/sql/AddDataToGroupsTable.sql")
    public void insert_ShouldMakeEntryInGroupsTable() {

        Group group = new Group();

        group.setCourseNumber(getCourse());
        group.setName("GRP-1234");
        group.setFaculty(getFaculty());

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        repository.saveAndFlush(group);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    @Sql("/sql/AddDataToGroupsTable.sql")
    public void insert_ShouldThrowException_WhenFacultyIdNotExist() {
        Faculty notExist = new Faculty();
        notExist.setId(BigInteger.valueOf(10));
        notExist.setName("NotExist");

        Group group = new Group();

        group.setId(ONE);
        group.setName("GRP-1234");
        group.setCourseNumber(getCourse());
        group.setFaculty(notExist);

        Assertions.assertThrows(DataAccessException.class,
                () -> repository.saveAndFlush(group));
    }

    @Test
    @Sql("/sql/AddDataToGroupsTable.sql")
    public void insert_ShouldThrowException_WhenCourseNumberIdNotExist() {
        CourseNumber notExist = new CourseNumber();
        notExist.setName("Notexist");
        notExist.setId(BigInteger.valueOf(10));

        Group group = new Group();

        group.setId(ONE);
        group.setName("GRP-1234");
        group.setCourseNumber(notExist);
        group.setFaculty(getFaculty());

        Assertions.assertThrows(DataAccessException.class,
                () -> repository.saveAndFlush(group));
    }

    @Test
    @Sql("/sql/AddDataToGroupsTable.sql")
    public void getById_ShouldReturnCorrectEntry_WhenEntryIsExist() {

        Group expectGroup = new Group();

        expectGroup.setId(BigInteger.valueOf(2));
        expectGroup.setName("GRP-1111");
        expectGroup.setFaculty(getFaculty());
        expectGroup.setCourseNumber(getCourse());

        Assertions.assertEquals(expectGroup, repository.findById(BigInteger.valueOf(2)).get());
    }

    @Test
    @Sql("/sql/AddDataToGroupsTable.sql")
    public void getAll_ShouldReturnListOfGroups_WhenConditionCompleted() {

        Group group = new Group();
        group.setId(BigInteger.valueOf(3));
        group.setName("GRP-2222");
        group.setCourseNumber(getCourse());
        group.setFaculty(getFaculty());

        List<Group> list = new ArrayList<>();
        list.add(group);

        Pageable pageable = PageRequest.of(1, 1, Sort.Direction.ASC, "id");
        Assertions.assertEquals(list, repository.findAll(pageable).getContent());
    }

    @Test
    @Sql("/sql/AddDataToGroupsTable.sql")
    public void update_ShouldUpdateEntry_WhenDataExist() {

        Group newValues = new Group();
        newValues.setId(BigInteger.valueOf(2));
        newValues.setName("NEW-1111");
        newValues.setCourseNumber(getCourse());
        newValues.setFaculty(getFaculty());

        repository.saveAndFlush(newValues);

        Assertions.assertEquals(newValues, repository.findById(BigInteger.valueOf(2)).get());
    }
    @Test
    @Sql("/sql/AddDataToGroupsTable.sql")
    public void update_ShouldThrowException_WhenDataNotExist() {

        Group newValues = new Group();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("NEW-1111");
        newValues.setCourseNumber(getCourse());
        newValues.setFaculty(getFaculty());

        Assertions.assertThrows(DataAccessException.class,
                () -> repository.saveAndFlush(newValues));
    }

    @Test
    @Sql("/sql/AddDataToGroupsTable.sql")
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");
        repository.deleteById(BigInteger.valueOf(2));
        repository.flush();
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "groups");

        Assertions.assertEquals(rowNumberBefore - 1, rowNumberAfter);

    }

    CourseNumber getCourse() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("Course");
        courseNumber.setId(ONE);

        return courseNumber;
    }

    Faculty getFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Faculty");
        faculty.setId(ONE);

        return faculty;
    }
}
