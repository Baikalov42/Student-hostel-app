package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.Equipment;

import java.math.BigInteger;
import java.util.List;

public interface EquipmentDao {
    BigInteger insert(Equipment equipment);

    void assignToStudent(BigInteger studentId, BigInteger equipmentId);

    void unassignFromStudent(BigInteger studentId, BigInteger equipmentId);

    Equipment getById(BigInteger equipmentId);

    List<Equipment> getAll(int offset, int limit);

    List<Equipment> getAllByStudent(BigInteger studentId);

    Equipment update(Equipment equipment);

    void deleteById(BigInteger id);
}
