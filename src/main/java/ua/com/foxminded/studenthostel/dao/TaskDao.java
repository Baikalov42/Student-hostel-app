package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.Task;

import java.math.BigInteger;
import java.util.List;

public interface TaskDao {
    BigInteger insert(Task task);

    Task getById(BigInteger taskId);

    List<Task> getAll(long limit, long offset);

    boolean assignToStudent(BigInteger taskId, BigInteger studentId);

    boolean removeFromStudent(BigInteger studentId, BigInteger taskId);

    boolean update(Task task);

    boolean deleteById(BigInteger id);
}
