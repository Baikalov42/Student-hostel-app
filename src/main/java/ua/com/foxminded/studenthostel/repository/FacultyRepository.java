package ua.com.foxminded.studenthostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.studenthostel.models.Faculty;

import java.math.BigInteger;


public interface FacultyRepository extends JpaRepository<Faculty, BigInteger> {
}
