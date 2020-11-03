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

    public void assignToStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("assigning, student id {}, equipment id {}", studentId, equipmentId);

        validator.validateId(studentId, equipmentId);
        validateExistence(equipmentId);
        studentService.validateExistence(studentId);

         equipmentDao.assignToStudent(studentId, equipmentId);
    }

    public void unassignFromStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("un assigning, student id {}, equipment id {}", studentId, equipmentId);

        validator.validateId(studentId, equipmentId);
        validateExistence(equipmentId);
        studentService.validateExistence(studentId);

         equipmentDao.unassignFromStudent(studentId, equipmentId);
    }


    public Equipment getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        Equipment equipment = equipmentDao.getById(id);

        LOGGER.debug("getting complete {}", equipment);
        return equipment;
    }


    public List<Equipment> getAll(int offset, int limit) {
        LOGGER.debug("getting all, offset {} , limit {} ", offset, limit);

        List<Equipment> result = equipmentDao.getAll(offset, limit);
        if (result.isEmpty()) {

            LOGGER.warn("result is empty, offset = {}, limit = {}", offset, limit);
            throw new NotFoundException("Result with offset=" + offset + " and limit=" + limit + " is empty");
        }
        return result;
    }

    public List<Equipment> getAllByStudent(BigInteger studentId) {
        LOGGER.debug("getting all by student id {} ", studentId);

        validator.validateId(studentId);
        studentService.validateExistence(studentId);

        List<Equipment> tasks = equipmentDao.getAllByStudent(studentId);

        if (tasks.isEmpty()) {
            LOGGER.warn("result is empty, student id = {}", studentId);
            throw new NotFoundException("result is empty, student id = " + studentId);
        }
        return tasks;
    }

    public Equipment update(Equipment equipment) {
        LOGGER.debug("updating {}", equipment);

        validator.validate(equipment);
        validator.validateId(equipment.getId());
        validateExistence(equipment.getId());

        return equipmentDao.update(equipment);
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

         equipmentDao.deleteById(id);
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
