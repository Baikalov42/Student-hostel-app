package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.Group;

import java.math.BigInteger;
import java.util.List;

public interface GroupDao {
    BigInteger insert(Group group);

    Group getById(BigInteger groupId);

    List<Group> getAll(int offset, int limit);

    Group update(Group group);

    void deleteById(BigInteger id);
}
