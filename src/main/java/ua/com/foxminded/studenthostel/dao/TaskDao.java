package ua.com.foxminded.studenthostel.dao;


import ua.com.foxminded.studenthostel.models.Task;

import java.math.BigInteger;
import java.util.List;

public interface TaskDao {
    BigInteger insert(Task task);

    Task getById(BigInteger taskId);

    List<Task> getAll(long limit, long offset);

    List<Task> getAllByStudent(BigInteger studentId);

    boolean assignToStudent(BigInteger studentId, BigInteger taskId);

    boolean unassignFromStudent(BigInteger studentId, BigInteger taskId);

    boolean isStudentTaskRelationExist(BigInteger studentId, BigInteger taskId);

    BigInteger getEntriesCount();

    boolean update(Task task);

    boolean deleteById(BigInteger id);
}
