package ua.com.foxminded.studenthostel.repository;

import java.math.BigInteger;

public interface TaskExtendedRepository {

    void assignToStudent(BigInteger studentId, BigInteger taskId);

    void unassignFromStudent(BigInteger studentId, BigInteger taskId);

    boolean isStudentTaskRelationExist(BigInteger studentId, BigInteger taskId);
}
