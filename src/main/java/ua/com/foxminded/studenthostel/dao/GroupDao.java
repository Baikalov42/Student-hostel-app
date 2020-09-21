package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.Group;

import java.math.BigInteger;
import java.util.List;

public interface GroupDao {
    BigInteger insert(Group group);

    Group getById(BigInteger groupId);

    List<Group> getAll(long limit, long offset);

    BigInteger getEntriesCount();

    boolean update(Group group);

    boolean deleteById(BigInteger id);
}
