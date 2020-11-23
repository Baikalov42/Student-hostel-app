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
import ua.com.foxminded.studenthostel.models.CourseNumber;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CourseNumberRepositoryTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private CourseNumberRepository repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void insert_ShouldMakeEntry_InCourseNumbersTable() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("First");

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");
        repository.saveAndFlush(courseNumber);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void insert_ShouldReturnId_WhenEntryIsInserted() {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("First");

        Assertions.assertEquals(ONE, repository.save(courseNumber).getId());
    }

    @Test
    @Sql("/sql/AddDataToCourseNumbersTable.sql")
    public void insert_ShouldThrowException_WhenNameNotUnique() {

        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName("First");

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(courseNumber));
    }

    @Test
    @Sql("/sql/AddDataToCourseNumbersTable.sql")
    public void getById_ShouldReturnCorrectEntry_WhenIdIsExist() {

        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(ONE);
        courseNumber.setName("First");

        Assertions.assertEquals(courseNumber, repository.findById(ONE).get());
    }

    @Test
    @Sql("/sql/AddDataToCourseNumbersTable.sql")
    public void getAll_ShouldReturnListOfCourseNumbers_WhenConditionCompleted() {

        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setId(BigInteger.valueOf(4));
        courseNumber.setName("Fourth");

        List<CourseNumber> courseNumbers = new ArrayList<>();
        courseNumbers.add(courseNumber);

        Pageable pageable = PageRequest.of(3, 1, Sort.Direction.ASC, "id");
        Assertions.assertEquals(courseNumbers, repository.findAll(pageable).getContent());
    }

    @Test
    @Sql("/sql/AddDataToCourseNumbersTable.sql")
    public void update_ShouldUpdateEntry_WhenDataExist() {

        CourseNumber newValues = new CourseNumber();
        newValues.setId(ONE);
        newValues.setName("Updated");

        repository.saveAndFlush(newValues);
        Assertions.assertEquals(newValues, repository.findById(ONE).get());
    }

    @Test
    @Sql("/sql/AddDataToCourseNumbersTable.sql")
    public void update_ShouldThrowException_WhenDataNotExist() {

        CourseNumber newValues = new CourseNumber();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("Updated");

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(newValues));
    }

    @Test
    @Sql("/sql/AddDataToCourseNumbersTable.sql")
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");
        repository.deleteById(BigInteger.valueOf(4));
        repository.flush();
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "course_numbers");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }
}
