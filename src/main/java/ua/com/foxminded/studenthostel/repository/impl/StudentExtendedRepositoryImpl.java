package ua.com.foxminded.studenthostel.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.studenthostel.repository.RoomRepository;
import ua.com.foxminded.studenthostel.repository.StudentRepository;
import ua.com.foxminded.studenthostel.repository.StudentExtendedRepository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.Student;

import javax.persistence.PersistenceException;
import java.math.BigInteger;

@Repository
@Transactional
public class StudentExtendedRepositoryImpl implements StudentExtendedRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentExtendedRepositoryImpl.class);

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student changeRoom(BigInteger newRoomId, BigInteger studentId) {
        LOGGER.debug("changeRoom, new room {}, student {}", newRoomId, studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found, id = " + studentId));

        Room newRoom = roomRepository.findById(newRoomId)
                .orElseThrow(() -> new NotFoundException("Room not found, id = " + newRoomId));

        student.setRoom(newRoom);

        try {
            studentRepository.flush();
            LOGGER.debug("changeRoom complete, result: {}", student);
            return student;

        } catch (DataAccessException ex) {
            LOGGER.error("changeRoom error student {}, room {}", studentId, newRoomId);
            throw new DaoException("changeRoom error student: + " + student + " room: " + newRoom);
        }
    }

    @Override
    public Student changeDebt(int newHoursDebt, BigInteger studentId) {
        LOGGER.debug("changeDebt, new debt {}, student {}", newHoursDebt, studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found, id = " + studentId));

        student.setHoursDebt(newHoursDebt);

        try {
            studentRepository.flush();

            LOGGER.debug("changeDebt complete, result: {}", student);
            return student;

        } catch (PersistenceException ex) {
            LOGGER.error("changeDebt error student {}, debt {}", studentId, newHoursDebt);
            throw new DaoException("changeDebt error student: + " + student + " debt: " + newHoursDebt);
        }
    }
}
