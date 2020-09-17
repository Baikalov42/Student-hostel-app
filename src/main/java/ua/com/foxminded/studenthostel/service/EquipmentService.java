package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.EquipmentDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
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

    public BigInteger insert(Equipment equipment) throws ValidationException {

        validator.validate(equipment);
        return equipmentDao.insert(equipment);
    }

    public boolean assignToStudent(BigInteger studentId, BigInteger equipmentId) throws ValidationException {

        validator.validateId(studentId, equipmentId);
        validateExistence(equipmentId);
        studentService.validateExistence(studentId);

        return equipmentDao.assignToStudent(studentId, equipmentId);
    }


    public boolean unassignFromStudent(BigInteger studentId, BigInteger equipmentId) throws ValidationException {

        validator.validateId(studentId, equipmentId);
        validateExistence(equipmentId);
        studentService.validateExistence(studentId);

        return equipmentDao.unassignFromStudent(studentId, equipmentId);
    }


    public Equipment getById(BigInteger equipmentId) throws ValidationException {

        validator.validateId(equipmentId);
        return equipmentDao.getById(equipmentId);
    }


    public List<Equipment> getAll(long limit, long offset) throws ValidationException {
        List<Equipment> result = equipmentDao.getAll(limit, offset);

        if (result.isEmpty()) {
            throw new ValidationException(
                    "Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean update(Equipment equipment) throws ValidationException {

        validator.validate(equipment);
        validator.validateId(equipment.getId());
        validateExistence(equipment.getId());

        return equipmentDao.update(equipment);
    }


    public boolean deleteById(BigInteger id) throws ValidationException {

        validator.validateId(id);
        validateExistence(id);

        return equipmentDao.deleteById(id);
    }

    protected void validateExistence(BigInteger id) throws ValidationException {
        try {
            equipmentDao.getById(id);
        } catch (DaoException ex) {
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
