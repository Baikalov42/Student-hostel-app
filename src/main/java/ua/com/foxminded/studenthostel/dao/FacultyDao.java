package ua.com.foxminded.studenthostel.dao;


import ua.com.foxminded.studenthostel.models.Faculty;

import java.math.BigInteger;
import java.util.List;

public interface FacultyDao {
    BigInteger insert(Faculty faculty);

    Faculty getById(BigInteger facultyId);

    List<Faculty> getAll(int offset, int limit);

    Faculty update(Faculty faculty);

    void deleteById(BigInteger id);
}
