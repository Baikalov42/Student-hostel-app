package ua.com.foxminded.studenthostel.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import ua.com.foxminded.studenthostel.models.Floor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FloorRepositoryTest {

    private static final BigInteger ONE = BigInteger.ONE;

    @Autowired
    private FloorRepository repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void insert_ShouldMakeEntry_InFloorsTable() {
        Floor floor = new Floor();
        floor.setName("Floor");

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");
        repository.saveAndFlush(floor);
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");

        Assertions.assertEquals(rowNumberBefore + 1, rowNumberAfter);
    }

    @Test
    public void insert_ShouldReturnId_WhenEntryIsInserted() {
        Floor floor = new Floor();
        floor.setName("Floor");
        Assertions.assertEquals(ONE, repository.saveAndFlush(floor).getId());
    }

    @Test
    @Sql("/sql/AddDataToFloorsTable.sql")
    public void insert_ShouldThrowException_WhenNameNotUnique() {

        Floor floor = new Floor();
        floor.setName("Floor");

        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(floor));
    }

    @Test
    @Sql("/sql/AddDataToFloorsTable.sql")
    public void getById_ShouldReturnFloor_WhenEntryIsExist() {

        Floor floor = new Floor();
        floor.setId(ONE);
        floor.setName("Floor");

        Assertions.assertEquals(floor, repository.findById(ONE).get());

    }

    @Test
    @Sql("/sql/AddDataToFloorsTable.sql")
    public void getAll_ShouldReturnListOfFloor_WhenConditionCompleted() {

        Floor floor = new Floor();
        floor.setName("Floorthree");
        floor.setId(BigInteger.valueOf(3));

        List<Floor> list = new ArrayList<>();
        list.add(floor);

        Pageable pageable = PageRequest.of(2, 1, Sort.Direction.ASC, "id");
        Assertions.assertEquals(list, repository.findAll(pageable).getContent());
    }

    @Test
    @Sql("/sql/AddDataToFloorsTable.sql")
    public void update_ShouldUpdateEntry_WhenDataExist() {

        Floor newValues = new Floor();
        newValues.setId(ONE);
        newValues.setName("Newname");

        repository.saveAndFlush(newValues);

        Assertions.assertEquals(newValues, repository.findById(ONE).get());
    }

    @Test
    @Sql("/sql/AddDataToFloorsTable.sql")
    public void update_ShouldThrowException_WhenDataNotExist() {

        Floor newValues = new Floor();
        newValues.setId(BigInteger.valueOf(7));
        newValues.setName("Newname");

        Assertions.assertThrows(DataAccessException.class,
                () -> repository.saveAndFlush(newValues));
    }

    @Test
    @Sql("/sql/AddDataToFloorsTable.sql")
    public void deleteById_ShouldDeleteEntry_WhenEntryIsExist() {

        int rowNumberBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");
        repository.deleteById(ONE);
        repository.flush();
        int rowNumberAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "floors");

        Assertions.assertEquals(rowNumberBefore - 1, rowNumberAfter);
    }
}
