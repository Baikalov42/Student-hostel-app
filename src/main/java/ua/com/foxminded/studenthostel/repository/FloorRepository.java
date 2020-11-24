package ua.com.foxminded.studenthostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.studenthostel.models.Floor;

import java.math.BigInteger;

public interface FloorRepository extends JpaRepository<Floor, BigInteger> {
}
