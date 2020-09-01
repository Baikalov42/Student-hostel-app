package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.Equipment;

import java.math.BigInteger;
import java.util.List;

public interface EquipmentDao {
    void save(Equipment equipment);

    boolean assignToStudent(BigInteger studentId, BigInteger equipmentId);

    boolean removeFromStudent(BigInteger studentId, BigInteger equipmentId);

    Equipment getById(BigInteger equipmentId);

    List<Equipment> getAll(long limit, long offset);
}
