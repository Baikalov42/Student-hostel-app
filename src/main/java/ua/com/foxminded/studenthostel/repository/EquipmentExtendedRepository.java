package ua.com.foxminded.studenthostel.repository;

import java.math.BigInteger;

public interface EquipmentExtendedRepository {

    void assignToStudent(BigInteger studentId, BigInteger equipmentId);

    void unassignFromStudent(BigInteger studentId, BigInteger equipmentId);
}
