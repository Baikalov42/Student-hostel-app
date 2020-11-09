package ua.com.foxminded.studenthostel.dao;


import ua.com.foxminded.studenthostel.models.Task;

import java.math.BigInteger;
import java.util.List;

public interface TaskDao {
    BigInteger insert(Task task);

    Task getById(BigInteger taskId);

    List<Task> getAll(int offset, int limit);

    List<Task> getAllByStudent(BigInteger studentId);

    void assignToStudent(BigInteger studentId, BigInteger taskId);

    void unassignFromStudent(BigInteger studentId, BigInteger taskId);

    boolean isStudentTaskRelationExist(BigInteger studentId, BigInteger taskId);

    Task update(Task task);

    void deleteById(BigInteger id);
}
