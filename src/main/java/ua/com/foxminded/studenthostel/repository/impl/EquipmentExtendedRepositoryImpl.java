package ua.com.foxminded.studenthostel.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.repository.EquipmentRepository;
import ua.com.foxminded.studenthostel.repository.EquipmentExtendedRepository;
import ua.com.foxminded.studenthostel.repository.StudentRepository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.Student;

import javax.persistence.PersistenceException;
import java.math.BigInteger;

@Repository
public class EquipmentExtendedRepositoryImpl implements EquipmentExtendedRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentExtendedRepositoryImpl.class);

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public void assignToStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("assigning, student {}, equipment {}", studentId, equipmentId);

        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new NotFoundException("not found equipment"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("not found student"));

        equipment.addStudent(student);

        try {
            studentRepository.flush();

        } catch (PersistenceException ex) {

            LOGGER.error("failed assigning, student id {}, equipment id {}", studentId, equipmentId, ex);
            String message = "student id =" + studentId + " equipment id =" + equipmentId;
            throw new DaoException(message, ex);
        }
    }

    @Override
    public void unassignFromStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("un assigning, student id {}, equipment id {}", studentId, equipmentId);

        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new NotFoundException("not found equipment"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("not found student"));

        equipment.removeStudent(student);
        try {
            studentRepository.flush();

        } catch (DataAccessException ex) {
            LOGGER.error("failed un assigning, student id {}, equipment id {}", studentId, equipmentId, ex);
            throw new DaoException("student id =" + studentId + " equipment id =" + equipmentId, ex);
        }
    }
}
