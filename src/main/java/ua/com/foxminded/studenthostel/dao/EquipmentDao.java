package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.Equipment;

import java.math.BigInteger;
import java.util.List;

public interface EquipmentDao {
    BigInteger insert(Equipment equipment);

    boolean assignToStudent(BigInteger studentId, BigInteger equipmentId);

    boolean unassignFromStudent(BigInteger studentId, BigInteger equipmentId);

    Equipment getById(BigInteger equipmentId);

    List<Equipment> getAll(long limit, long offset);

    List<Equipment> getAllByStudent(BigInteger studentId);

    boolean update(Equipment equipment);

    boolean deleteById(BigInteger id);
}
