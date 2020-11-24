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
import ua.com.foxminded.studenthostel.models.Faculty;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FacultyRepositoryTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private FacultyRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void insert_ShouldMakeEntry_InFacultiesTable() {
        Faculty faculty = new Faculty();
        faculty.setName("Web design");

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");
        repository.saveAndFlush(faculty);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void insert_ShouldReturnId_WhenEntryIsInserted() {
        Faculty faculty = new Faculty();
        faculty.setName("Web design");
        Assertions.assertEquals(ONE, repository.saveAndFlush(faculty).getId());
    }

    @Test
    @Sql("/sql/AddDataToFacultiesTable.sql")
    public void insert_ShouldThrowException_WhenNameNotUnique() {

        Faculty faculty = new Faculty();
        faculty.setName("Web design");

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(faculty));
    }

    @Test
    @Sql("/sql/AddDataToFacultiesTable.sql")
    public void getById_ShouldReturnFaculty_WhenEntryIsExist() {

        Faculty faculty = new Faculty();
        faculty.setId(ONE);
        faculty.setName("Web design");
        Assertions.assertEquals(faculty, repository.findById(ONE).get());
    }

    @Test
    @Sql("/sql/AddDataToFacultiesTable.sql")
    public void getAll_ShouldReturnListOfFaculties_WhenConditionCompleted() {

        Faculty faculty = new Faculty();
        faculty.setId(BigInteger.valueOf(4));
        faculty.setName("Front end development");

        List<Faculty> list = new ArrayList<>();
        list.add(faculty);

        Pageable pageable = PageRequest.of(3, 1, Sort.Direction.ASC, "id");
        Assertions.assertEquals(list, repository.findAll(pageable).getContent());
    }

    @Test
    @Sql("/sql/AddDataToFacultiesTable.sql")
    public void update_ShouldUpdateEntry_WhenDataExist() {

        Faculty newValues = new Faculty();
        newValues.setId(ONE);
        newValues.setName("Newname");

        repository.saveAndFlush(newValues);

        Assertions.assertEquals(newValues, repository.findById(ONE).get());
    }

    @Test
    @Sql("/sql/AddDataToFacultiesTable.sql")
    public void update_ShouldThrowException_WhenDataNotExist() {

        Faculty newValues = new Faculty();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("Newname");

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(newValues));
    }

    @Test
    @Sql("/sql/AddDataToFacultiesTable.sql")
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {

        int rowBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");
        repository.deleteById(ONE);
        repository.flush();
        int rowAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "faculties");

        Assertions.assertEquals(rowBefore - 1, rowAfter);
    }
}
