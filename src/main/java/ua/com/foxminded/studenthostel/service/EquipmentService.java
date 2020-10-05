package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentService.class);

    @Autowired
    private EquipmentDao equipmentDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ValidatorEntity<Equipment> validator;

    public BigInteger insert(Equipment equipment) {
        LOGGER.debug("inserting {}", equipment);

        validator.validate(equipment);
        BigInteger id = equipmentDao.insert(equipment);

        LOGGER.debug("inserting complete, id = {}", id);
        return id;
    }

    public boolean assignToStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("assigning, student id {}, equipment id {}", studentId, equipmentId);

        validator.validateId(studentId, equipmentId);
        validateExistence(equipmentId);
        studentService.validateExistence(studentId);

        return equipmentDao.assignToStudent(studentId, equipmentId);
    }

    public boolean unassignFromStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("un assigning, student id {}, equipment id {}", studentId, equipmentId);

        validator.validateId(studentId, equipmentId);
        validateExistence(equipmentId);
        studentService.validateExistence(studentId);

        return equipmentDao.unassignFromStudent(studentId, equipmentId);
    }


    public Equipment getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        Equipment equipment = equipmentDao.getById(id);

        LOGGER.debug("getting complete {}", equipment);
        return equipment;
    }


    public List<Equipment> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);

        List<Equipment> result = equipmentDao.getAll(limit, offset);
        if (result.isEmpty()) {

            LOGGER.warn("result is empty, limit = {}, offset = {}", limit, offset);
            throw new NotFoundException("Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public boolean update(Equipment equipment) {
        LOGGER.debug("updating {}", equipment);

        validator.validate(equipment);
        validator.validateId(equipment.getId());
        validateExistence(equipment.getId());

        return equipmentDao.update(equipment);
    }

    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        return equipmentDao.deleteById(id);
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);
        try {
            equipmentDao.getById(id);

        } catch (NotFoundException ex) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }
}
