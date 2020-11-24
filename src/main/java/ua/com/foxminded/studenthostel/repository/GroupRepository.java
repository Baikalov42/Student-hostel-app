package ua.com.foxminded.studenthostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.studenthostel.models.Group;

import java.math.BigInteger;

public interface GroupRepository extends JpaRepository<Group, BigInteger> {
}
