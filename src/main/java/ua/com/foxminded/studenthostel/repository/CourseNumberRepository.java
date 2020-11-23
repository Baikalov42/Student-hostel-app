package ua.com.foxminded.studenthostel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.studenthostel.models.CourseNumber;

import java.math.BigInteger;

public interface CourseNumberRepository extends JpaRepository<CourseNumber, BigInteger> {

}
