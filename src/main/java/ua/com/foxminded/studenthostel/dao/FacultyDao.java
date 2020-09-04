package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.Faculty;

import java.math.BigInteger;
import java.util.List;

public interface FacultyDao {
    BigInteger insert(Faculty faculty);

    Faculty getById(BigInteger facultyId);

    List<Faculty> getAll(long limit, long offset);

    boolean deleteById(BigInteger id);
}
