package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import ua.com.foxminded.studenthostel.repository.StudentRepository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Service
public class StudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);
    private static final int PAGE_SIZE = 10;
    public static final int MAX_HOURS_DEBT = 40;
    public static final int MIN_HOURS_DEBT = 0;
    public static final int MAX_STUDENTS_IN_ROOM = 4;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoomService roomService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CourseNumberService courseNumberService;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private FloorService floorService;

    @Autowired
    private ValidatorEntity<Student> validator;


    public BigInteger insert(Student student) {
        LOGGER.debug("inserting {}", student);

        validator.validate(student);

        groupService.validateExistence(student.getGroup().getId());
        roomService.validateExistence(student.getRoom().getId());

        validateRoomVacancy(student.getRoom().getId());

        try {
            return studentRepository.save(student).getId();

        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", student, ex);
            throw new DaoException("Insertion error : " + student, ex);
        }
    }

    public Student getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);

        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student not found, id = " + id));
    }

    public List<Student> getAll(int pageNumber) {
        LOGGER.debug("getting all, pageNumber = {}, pageSize = {} ", pageNumber, PAGE_SIZE);

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "id");
        List<Student> result = studentRepository.findAll(pageable).getContent();

        if (result.isEmpty()) {
            LOGGER.warn("result is empty, pageNumber = {} ", pageNumber);
            throw new NotFoundException("Result with pageNumber =" + pageNumber + " is empty");
        }
        return result;
    }

    public List<Student> getAllByFloor(BigInteger floorId) {
        LOGGER.debug("getting all by floor id {} ", floorId);

        validator.validateId(floorId);
        floorService.validateExistence(floorId);

        List<Student> students = studentRepository.getAllByFloor(floorId);

        if (students.isEmpty()) {
            LOGGER.warn("result is empty, floor id = {}", floorId);
            throw new NotFoundException("result is empty, floor id = " + floorId);
        }
        return students;
    }

    public List<Student> getAllByFaculty(BigInteger facultyId) {
        LOGGER.debug("getting all by faculty id {} ", facultyId);

        validator.validateId(facultyId);
        facultyService.validateExistence(facultyId);

        List<Student> students = studentRepository.getAllByFaculty(facultyId);

        if (students.isEmpty()) {
            LOGGER.warn("result is empty, faculty id = {}", facultyId);
            throw new NotFoundException("result is empty, faculty id = " + facultyId);
        }
        return students;
    }

    public List<Student> getAllByCourse(BigInteger courseNumberId) {
        LOGGER.debug("getting all by course id {} ", courseNumberId);

        validator.validateId(courseNumberId);
        courseNumberService.validateExistence(courseNumberId);

        List<Student> students = studentRepository.getAllByCourse(courseNumberId);

        if (students.isEmpty()) {
            LOGGER.warn("result is empty, course id = {}", courseNumberId);
            throw new NotFoundException("result is empty, course id = " + courseNumberId);
        }
        return students;
    }

    public List<Student> getAllWithDebitByGroup(BigInteger groupId, int hoursDebt) {
        LOGGER.debug("getting all by Group id ={} , with debt = {} ", groupId, hoursDebt);

        validator.validateId(groupId);
        groupService.validateExistence(groupId);

        List<Student> students = studentRepository.getAllWithDebitByGroup(groupId, hoursDebt);

        if (students.isEmpty()) {
            LOGGER.warn("result is empty, group id = {}, debt = {}", groupId, hoursDebt);
            throw new NotFoundException("result is empty, group id = " + groupId + "debt = " + hoursDebt);
        }
        return students;
    }

    public Student changeRoom(BigInteger newRoomId, BigInteger studentId) {
        LOGGER.debug("changing room id = {}, student id = {}", newRoomId, studentId);

        validator.validateId(newRoomId, studentId);
        validateExistence(studentId);
        roomService.validateExistence(newRoomId);

        validateRoomVacancy(newRoomId);

        return studentRepository.changeRoom(newRoomId, studentId);
    }

    public Student changeDebt(Integer newHoursDebt, BigInteger studentId) {
        LOGGER.debug("changing debt, new debt = {}, student id = {}", newHoursDebt, studentId);

        if (newHoursDebt > MAX_HOURS_DEBT || newHoursDebt < MIN_HOURS_DEBT) {
            LOGGER.warn("not valid debt. debt = {}, student id ={}", newHoursDebt, studentId);
            throw new ValidationException("the value must be in the range from 0 to 40");
        }
        validateExistence(studentId);
        return studentRepository.changeDebt(newHoursDebt, studentId);
    }

    public Student update(Student student) {
        LOGGER.debug("updating {}", student);

        validator.validate(student);
        validator.validateId(student.getId());

        validateExistence(student.getId());
        groupService.validateExistence(student.getGroup().getId());
        roomService.validateExistence(student.getRoom().getId());

        try {
            return studentRepository.save(student);

        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", student, ex);
            throw new DaoException("Updating error: " + student, ex);
        }
    }

    public int acceptTaskAndUpdateHours(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("accept hours and update: student id ={}, task id={}", studentId, taskId);

        validator.validateId(studentId, taskId);

        this.validateExistence(studentId);
        taskService.validateExistence(taskId);

        if (!taskService.isStudentTaskRelationExist(studentId, taskId)) {
            LOGGER.warn("relation between student and task not exist. Student id ={}, Task id = {}", studentId, taskId);
            throw new ValidationException("Relation student id = " + studentId + " and task id = " + taskId + " not exist");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found, id = " + studentId));

        Task task = taskService.getById(taskId);

        int newHoursDebt = student.getHoursDebt() - task.getCostInHours();
        if (newHoursDebt < 0) {
            newHoursDebt = 0;
        }
        student.setHoursDebt(newHoursDebt);

        taskService.unassignFromStudent(studentId, taskId);
        studentRepository.save(student);

        LOGGER.debug("accept hours and update is complete, new hours debt ={}", newHoursDebt);

        return newHoursDebt;
    }

    public void deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        try {
            studentRepository.deleteById(id);

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);

        if (!studentRepository.existsById(id)) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist");
        }
    }

    private void validateRoomVacancy(BigInteger roomId) {
        LOGGER.debug("Room vacancy validation, room id = {}", roomId);
        Set<Student> students = roomService.getById(roomId).getStudents();

        if (students.size() >= MAX_STUDENTS_IN_ROOM) {

            LOGGER.warn("validation error, room id ={}", roomId);
            throw new ValidationException("no more than 4 student in one room");
        }
    }
}
