package ua.com.foxminded.studenthostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.studenthostel.models.Task;

import java.math.BigInteger;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, BigInteger> , TaskExtendedRepository {

    @Query("select t " +
            "from Task t " +
            "inner join t.students st " +
            "where st.id = :id")
    List<Task> getAllByStudent(@Param("id") BigInteger studentId);
}
