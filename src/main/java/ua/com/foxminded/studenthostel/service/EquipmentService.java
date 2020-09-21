package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.EquipmentDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentDao equipmentDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ValidatorEntity<Equipment> validator;

    public BigInteger insert(Equipment equipment) {

        validator.validate(equipment);
        return equipmentDao.insert(equipment);
    }

    public boolean assignToStudent(BigInteger studentId, BigInteger equipmentId) {

        validator.validateId(studentId, equipmentId);
        validateExistence(equipmentId);
        studentService.validateExistence(studentId);

        return equipmentDao.assignToStudent(studentId, equipmentId);
    }

    public boolean unassignFromStudent(BigInteger studentId, BigInteger equipmentId) {

        validator.validateId(studentId, equipmentId);
        validateExistence(equipmentId);
        studentService.validateExistence(studentId);

        return equipmentDao.unassignFromStudent(studentId, equipmentId);
    }


    public Equipment getById(BigInteger equipmentId) {

        validator.validateId(equipmentId);
        return equipmentDao.getById(equipmentId);
    }


    public List<Equipment> getAll(long limit, long offset) {
        List<Equipment> result = equipmentDao.getAll(limit, offset);

        if (result.isEmpty()) {
            throw new NotFoundException(
                    "Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean update(Equipment equipment) {

        validator.validate(equipment);
        validator.validateId(equipment.getId());
        validateExistence(equipment.getId());

        return equipmentDao.update(equipment);
    }

    public boolean deleteById(BigInteger id) {

        validator.validateId(id);
        validateExistence(id);

        return equipmentDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        try {
            equipmentDao.getById(id);
        } catch (NotFoundException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}