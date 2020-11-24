package ua.com.foxminded.studenthostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.studenthostel.models.Equipment;

import java.math.BigInteger;
import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, BigInteger>, EquipmentExtendedRepository {

    @Query(" select eq " +
            "from Equipment eq " +
            "join eq.students st " +
            "where st.id =:id")
    List<Equipment> findAllByStudent(@Param("id") BigInteger studentId);
}
