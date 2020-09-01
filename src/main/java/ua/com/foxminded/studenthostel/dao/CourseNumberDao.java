package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.CourseNumber;

import java.math.BigInteger;
import java.util.List;

public interface CourseNumberDao {
    void save(CourseNumber courseNumber);

    CourseNumber getById(BigInteger courseNumberId);

    List<CourseNumber> getAll(long limit, long offset);
}
