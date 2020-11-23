package ua.com.foxminded.studenthostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.studenthostel.models.Room;

import java.math.BigInteger;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, BigInteger> {
    @Query("select rm " +
            "from Room rm " +
            "join rm.students st " +
            "join  st.equipments eq " +
            "where eq.id = :id ")
    List<Room> getAllByEquipment(@Param("id") BigInteger equipmentId);
}
