package ua.com.foxminded.studenthostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.studenthostel.models.Student;

import java.math.BigInteger;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, BigInteger>, StudentExtendedRepository {

    @Query("select st " +
            "from Student st " +
            "join st.room ro " +
            "join ro.floor fl " +
            "where fl.id =:id")
    List<Student> getAllByFloor(@Param("id") BigInteger floorId);

    @Query("select st " +
            "from Student st " +
            "join st.group gr " +
            "join gr.faculty fa " +
            "where fa.id = :id")
    List<Student> getAllByFaculty(@Param("id") BigInteger facultyId);

    @Query("select st " +
            "from Student st " +
            "join st.group gr " +
            "join gr.courseNumber cn " +
            "where cn.id = :id ")
    List<Student> getAllByCourse(@Param("id") BigInteger courseId);

    @Query("select st " +
            "from Student st " +
            "join st.group gr " +
            "where gr.id = :id " +
            "and st.hoursDebt > :debt ")
    List<Student> getAllWithDebitByGroup(@Param("id") BigInteger groupId,
                                         @Param("debt") int numberOfHoursDebt);
}
