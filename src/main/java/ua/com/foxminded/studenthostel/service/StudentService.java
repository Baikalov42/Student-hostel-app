package ua.com.foxminded.studenthostel.service;

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

        validator.validate(student);

        groupService.validateExistence(student.getGroupId());
        roomService.validateExistence(student.getRoomId());

        validateRoomVacancy(student.getRoomId());

        return studentDao.insert(student);
    }

    public Student getById(BigInteger id) {

        validator.validateId(id);
        return studentDao.getById(id);
    }

    public StudentDTO getDTOById(BigInteger id) {

        Student student = getById(id);
        return getDTO(student);
    }

    public List<Student> getAll(long limit, long offset) {

        List<Student> result = studentDao.getAll(limit, offset);
        if (result.isEmpty()) {
            throw new NotFoundException(
                    "Result with limit=" + limit + " and offset=" + offset + " is empty");
        }
        return result;
    }

    public List<StudentDTO> getAllDTO(long limit, long offset) {

        List<Student> result = getAll(limit, offset);
        return getDTOS(result);
    }

    public List<StudentDTO> getAllByFloor(BigInteger floorId) {

        validator.validateId(floorId);
        floorService.validateExistence(floorId);

        List<Student> students = studentDao.getAllByFloor(floorId);

        if (students.isEmpty()) {
            throw new NotFoundException(
                    "Result with floor id=" + floorId + " is empty");
        }
        return getDTOS(students);
    }

    public List<StudentDTO> getAllByFaculty(BigInteger facultyId) {

        validator.validateId(facultyId);
        facultyService.validateExistence(facultyId);

        List<Student> students = studentDao.getAllByFaculty(facultyId);

        if (students.isEmpty()) {
            throw new NotFoundException(
                    "Result with faculty id=" + facultyId + " is empty");
        }

        return getDTOS(students);
    }

    public List<StudentDTO> getAllByCourse(BigInteger courseNumberId) {

        validator.validateId(courseNumberId);
        courseNumberService.validateExistence(courseNumberId);

        List<Student> students = studentDao.getAllByCourse(courseNumberId);

        if (students.isEmpty()) {
            throw new NotFoundException(
                    "Result with faculty id=" + courseNumberId + " is empty");
        }

        return getDTOS(students);
    }

    public List<StudentDTO> getAllWithDebitByGroup(BigInteger groupId, int hoursDebt) {

        validator.validateId(groupId);
        groupService.validateExistence(groupId);

        List<Student> students = studentDao.getAllWithDebitByGroup(groupId, hoursDebt);

        if (students.isEmpty()) {
            throw new NotFoundException(
                    "Result with group id=" + groupId + " and debt=" + hoursDebt + " is empty");
        }

        return getDTOS(students);
    }

    public boolean changeRoom(BigInteger newRoomId, BigInteger studentId) {

        validator.validateId(newRoomId, studentId);
        validateExistence(studentId);
        roomService.validateExistence(newRoomId);

        validateRoomVacancy(newRoomId);

        return studentDao.changeRoom(newRoomId, studentId);
    }

    public boolean changeDebt(Integer newHoursDebt, BigInteger studentId) {
        if (newHoursDebt > MAX_HOURS_DEBT || newHoursDebt < MIN_HOURS_DEBT) {
            throw new ValidationException("the value must be in the range from 0 to 40");
        }
        validateExistence(studentId);
        return studentDao.changeDebt(newHoursDebt, studentId);
    }

    public boolean update(Student student) {

        validator.validate(student);
        validator.validateId(student.getId());

        validateExistence(student.getId());
        groupService.validateExistence(student.getGroupId());
        roomService.validateExistence(student.getRoomId());

        return studentDao.update(student);
    }

    public int acceptHoursAndUpdate(BigInteger studentId, BigInteger taskId) {
        validator.validateId(studentId, taskId);

        this.validateExistence(studentId);
        taskService.validateExistence(taskId);

        if (!taskService.isStudentTaskRelationExist(studentId, taskId)) {
            throw new ValidationException(
                    "Relation student id = " + studentId + " and task id = " + taskId + " not exist");
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
        return newHoursDebt;
    }

    public boolean deleteById(BigInteger id) {

        validator.validateId(id);
        validateExistence(id);

        return studentDao.deleteById(id);
    }

    StudentDTO getDTO(Student student) {

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());

        studentDTO.setHoursDebt(student.getHoursDebt());
        studentDTO.setRoomDTO(roomService.getDTO(roomDao.getById(student.getRoomId())));
        studentDTO.setGroupDTO(groupService.getDTO(groupDao.getById(student.getGroupId())));

        studentDTO.setEquipments(equipmentDao.getAllByStudent(student.getId()));
        studentDTO.setTasks(taskDao.getAllByStudent(student.getId()));

        return studentDTO;
    }

    List<StudentDTO> getDTOS(List<Student> students) {
        List<StudentDTO> studentDTOS = new ArrayList<>(students.size());
        for (Student student : students) {
            studentDTOS.add(getDTO(student));
        }
        return studentDTOS;
    }

    void validateExistence(BigInteger studentId) {
        try {
            studentDao.getById(studentId);
        } catch (NotFoundException ex) {
            throw new ValidationException("student not exist", ex);
        }
    }

    private void validateRoomVacancy(BigInteger roomId) {

        if (studentDao.getStudentsCountByRoom(roomId) >= MAX_STUDENTS_IN_ROOM) {
            throw new ValidationException("no more than 4 student in one room");
        }
    }
}
