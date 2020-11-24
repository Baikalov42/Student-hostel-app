package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.repository.EquipmentRepository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;

@Service
public class EquipmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentService.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ValidatorEntity<Equipment> validator;

    public BigInteger insert(Equipment equipment) {
        LOGGER.debug("inserting {}", equipment);

        validator.validate(equipment);
        try {
            return equipmentRepository.save(equipment).getId();

        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", equipment, ex);
            throw new DaoException("Insertion error : " + equipment, ex);
        }
    }

    public void assignToStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("assigning, student id {}, equipment id {}", studentId, equipmentId);

        validator.validateId(studentId, equipmentId);
        this.validateExistence(equipmentId);
        studentService.validateExistence(studentId);

        equipmentRepository.assignToStudent(studentId, equipmentId);
    }

    public void unassignFromStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("un assigning, student id {}, equipment id {}", studentId, equipmentId);

        validator.validateId(studentId, equipmentId);
        this.validateExistence(equipmentId);
        studentService.validateExistence(studentId);

        equipmentRepository.unassignFromStudent(studentId, equipmentId);
    }

    public Equipment getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found, id = " + id));
    }

    public List<Equipment> getAll(int pageNumber) {
        LOGGER.debug("getting all, pageNumber = {}, pageSize = {} ", pageNumber, PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<Equipment> result = equipmentRepository.findAll(pageable).getContent();

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, pageNumber = {} ", pageNumber);
            throw new NotFoundException("Result with pageNumber =" + pageNumber + " is empty");
        }
        return result;
    }

    public List<Equipment> getAllByStudent(BigInteger studentId) {
        LOGGER.debug("getting all by student id {} ", studentId);

        validator.validateId(studentId);
        studentService.validateExistence(studentId);

        List<Equipment> equipment = equipmentRepository.findAllByStudent(studentId);

        if (equipment.isEmpty()) {
            LOGGER.warn("result is empty, student id = {}", studentId);
            throw new NotFoundException("result is empty, student id = " + studentId);
        }
        return equipment;
    }

    public Equipment update(Equipment equipment) {
        LOGGER.debug("updating {}", equipment);

        validator.validate(equipment);
        validator.validateId(equipment.getId());
        validateExistence(equipment.getId());

        try {
            return equipmentRepository.save(equipment);
        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", equipment, ex);
            throw new DaoException("Updating error: " + equipment, ex);
        }
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);
        try {
            equipmentRepository.deleteById(id);
        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);

        if (!equipmentRepository.existsById(id)) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist");
        }
    }
}
