package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.EquipmentDao;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.service.utils.PatternValidator;

import java.math.BigInteger;
import java.util.List;

@Service
public class EquipmentService {

    private static final String NAME_PATTERN = "[A-Z][a-z\\s]{3,29}";

    @Autowired
    EquipmentDao equipmentDao;


    public BigInteger insert(Equipment equipment) throws ValidationException {
        PatternValidator.validateName(equipment.getName(), NAME_PATTERN);
        return equipmentDao.insert(equipment);
    }

    public boolean assignToStudent(BigInteger studentId, BigInteger equipmentId) {
        return equipmentDao.assignToStudent(studentId, equipmentId);
    }


    public boolean unassignFromStudent(BigInteger studentId, BigInteger equipmentId) {
        return equipmentDao.unassignFromStudent(studentId, equipmentId);
    }


    public Equipment getById(BigInteger equipmentId) {
        if (equipmentId == null || equipmentId.longValue() == 0) {
            throw new IllegalArgumentException();
        }
        return equipmentDao.getById(equipmentId);
    }


    public List<Equipment> getAll(long limit, long offset) {
        return equipmentDao.getAll(limit, offset);
    }

    public boolean update(Equipment equipment) throws ValidationException {
        PatternValidator.validateName(equipment.getName(), NAME_PATTERN);
        return equipmentDao.update(equipment);
    }


    public boolean deleteById(BigInteger id) {
        return equipmentDao.deleteById(id);
    }

}
