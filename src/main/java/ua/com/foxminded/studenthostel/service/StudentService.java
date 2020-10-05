package ua.com.foxminded.studenthostel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.studenthostel.dao.EquipmentDao;
import ua.com.foxminded.studenthostel.dao.GroupDao;
import ua.com.foxminded.studenthostel.dao.RoomDao;
import ua.com.foxminded.studenthostel.dao.StudentDao;
import ua.com.foxminded.studenthostel.dao.TaskDao;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.models.dto.StudentDTO;
import ua.com.foxminded.studenthostel.service.utils.ValidatorEntity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    public static final int MAX_HOURS_DEBT = 40;
    public static final int MIN_HOURS_DEBT = 0;
    public static final int MAX_STUDENTS_IN_ROOM = 4;

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private EquipmentDao equipmentDao;
    @Autowired
    private TaskDao taskDao;
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

        groupService.validateExistence(student.getGroupId());
        roomService.validateExistence(student.getRoomId());

        validateRoomVacancy(student.getRoomId());

        BigInteger id = studentDao.insert(student);

        LOGGER.debug("inserting complete, id = {}", id);
        return id;
    }

    public Student getById(BigInteger id) {
        LOGGER.debug("getting by id {}", id);

        validator.validateId(id);
        Student student = studentDao.getById(id);

        LOGGER.debug("getting complete {}", student);
        return student;
    }

    public StudentDTO getDTOById(BigInteger id) {
        LOGGER.debug("getting DTO by id {}", id);

        Student student = getById(id);
        StudentDTO studentDTO = getDTO(student);

        LOGGER.debug("getting DTO by id, complete {}", studentDTO);
        return studentDTO;
    }

    public List<Student> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);

        List<Student> result = studentDao.getAll(limit, offset);
        if (result.isEmpty()) {

            LOGGER.warn("result is empty, limit = {}, offset = {}", limit, offset);
            throw new NotFoundException("Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public List<StudentDTO> getAllDTO(long limit, long offset) {
        LOGGER.debug("getting all DTO, limit {} , offset {} ", limit, offset);

        List<Student> result = getAll(limit, offset);
        return getDTOS(result);
    }

    public List<StudentDTO> getAllByFloor(BigInteger floorId) {
        LOGGER.debug("getting all by floor id {} ", floorId);

        validator.validateId(floorId);
        floorService.validateExistence(floorId);

        List<Student> students = studentDao.getAllByFloor(floorId);

        if (students.isEmpty()) {
            LOGGER.warn("result is empty, floor id = {}", floorId);
            throw new NotFoundException("result is empty, floor id = " + floorId);
        }
        return getDTOS(students);
    }

    public List<StudentDTO> getAllByFaculty(BigInteger facultyId) {
        LOGGER.debug("getting all by faculty id {} ", facultyId);

        validator.validateId(facultyId);
        facultyService.validateExistence(facultyId);

        List<Student> students = studentDao.getAllByFaculty(facultyId);

        if (students.isEmpty()) {
            LOGGER.warn("result is empty, faculty id = {}", facultyId);
            throw new NotFoundException("result is empty, faculty id = " + facultyId);
        }

        return getDTOS(students);
    }

    public List<StudentDTO> getAllByCourse(BigInteger courseNumberId) {
        LOGGER.debug("getting all by course id {} ", courseNumberId);

        validator.validateId(courseNumberId);
        courseNumberService.validateExistence(courseNumberId);

        List<Student> students = studentDao.getAllByCourse(courseNumberId);

        if (students.isEmpty()) {
            LOGGER.warn("result is empty, course id = {}", courseNumberId);
            throw new NotFoundException("result is empty, course id = " + courseNumberId);
        }

        return getDTOS(students);
    }

    public List<StudentDTO> getAllWithDebitByGroup(BigInteger groupId, int hoursDebt) {
        LOGGER.debug("getting all by Group id ={} , with debt = {} ", groupId, hoursDebt);

        validator.validateId(groupId);
        groupService.validateExistence(groupId);

        List<Student> students = studentDao.getAllWithDebitByGroup(groupId, hoursDebt);

        if (students.isEmpty()) {
            LOGGER.warn("result is empty, group id = {}, debt = {}", groupDao, hoursDebt);
            throw new NotFoundException("result is empty, group id = " + groupId + "debt = " + hoursDebt);
        }

        return getDTOS(students);
    }

    public boolean changeRoom(BigInteger newRoomId, BigInteger studentId) {
        LOGGER.debug("changing room id = {}, student id = {}", newRoomId, studentId);

        validator.validateId(newRoomId, studentId);
        validateExistence(studentId);
        roomService.validateExistence(newRoomId);

        validateRoomVacancy(newRoomId);

        return studentDao.changeRoom(newRoomId, studentId);
    }

    public boolean changeDebt(Integer newHoursDebt, BigInteger studentId) {
        LOGGER.debug("changing debt, new debt = {}, student id = {}", newHoursDebt, studentId);

        if (newHoursDebt > MAX_HOURS_DEBT || newHoursDebt < MIN_HOURS_DEBT) {
            LOGGER.warn("not valid debt. debt = {}, student id ={}", newHoursDebt, studentId);
            throw new ValidationException("the value must be in the range from 0 to 40");
        }
        validateExistence(studentId);
        return studentDao.changeDebt(newHoursDebt, studentId);
    }

    public boolean update(Student student) {
        LOGGER.debug("updating {}", student);

        validator.validate(student);
        validator.validateId(student.getId());

        validateExistence(student.getId());
        groupService.validateExistence(student.getGroupId());
        roomService.validateExistence(student.getRoomId());

        return studentDao.update(student);
    }

    public int acceptHoursAndUpdate(BigInteger studentId, BigInteger taskId) {
        LOGGER.debug("accept hours and update: student id ={}, task id={}", studentId, taskId);

        validator.validateId(studentId, taskId);

        this.validateExistence(studentId);
        taskService.validateExistence(taskId);

        if (!taskService.isStudentTaskRelationExist(studentId, taskId)) {
            LOGGER.warn("relation between student and task not exist. Student id ={}, Task id = {}", studentId, taskId);
            throw new ValidationException("Relation student id = " + studentId + " and task id = " + taskId + " not exist");
        }

        Student student = studentDao.getById(studentId);
        Task task = taskDao.getById(taskId);

        int newHoursDebt = student.getHoursDebt() - task.getCostInHours();
        if (newHoursDebt < 0) {
            newHoursDebt = 0;
        }
        student.setHoursDebt(newHoursDebt);

        taskDao.unassignFromStudent(studentId, taskId);
        studentDao.update(student);

        LOGGER.debug("accept hours and update is complete, new hours debt ={}", newHoursDebt);

        return newHoursDebt;
    }

    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        validator.validateId(id);
        validateExistence(id);

        return studentDao.deleteById(id);
    }

    StudentDTO getDTO(Student student) {
        LOGGER.debug("getting DTO,  {}", student);

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());

        studentDTO.setHoursDebt(student.getHoursDebt());
        studentDTO.setRoomDTO(roomService.getDTO(roomDao.getById(student.getRoomId())));
        studentDTO.setGroupDTO(groupService.getDTO(groupDao.getById(student.getGroupId())));

        studentDTO.setEquipments(equipmentDao.getAllByStudent(student.getId()));
        studentDTO.setTasks(taskDao.getAllByStudent(student.getId()));

        LOGGER.debug("getting DTO complete,  {}", studentDTO);
        return studentDTO;
    }

    List<StudentDTO> getDTOS(List<Student> students) {
        List<StudentDTO> studentDTOS = new ArrayList<>(students.size());
        for (Student student : students) {
            studentDTOS.add(getDTO(student));
        }
        return studentDTOS;
    }

    void validateExistence(BigInteger id) {
        LOGGER.debug("Validation existence id = {}", id);
        try {
            studentDao.getById(id);

        } catch (NotFoundException ex) {
            LOGGER.warn("entry not exist, id = {}", id);
            throw new ValidationException("id = " + id + " not exist", ex);
        }
    }

    private void validateRoomVacancy(BigInteger roomId) {
        LOGGER.debug("Room vacancy validation, room id = {}", roomId);

        if (studentDao.getStudentsCountByRoom(roomId) >= MAX_STUDENTS_IN_ROOM) {

            LOGGER.warn("validation error, room id ={}", roomId);
            throw new ValidationException("no more than 4 student in one room");
        }
    }
}
