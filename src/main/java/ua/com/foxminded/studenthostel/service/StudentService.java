package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.studenthostel.dao.EquipmentDao;
import ua.com.foxminded.studenthostel.dao.FacultyDao;
import ua.com.foxminded.studenthostel.dao.GroupDao;
import ua.com.foxminded.studenthostel.dao.RoomDao;
import ua.com.foxminded.studenthostel.dao.StudentDao;
import ua.com.foxminded.studenthostel.dao.TaskDao;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.dto.StudentDTO;
import ua.com.foxminded.studenthostel.service.utils.ValidationsUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private static final String NAME_PATTERN = "[A-Z][a-z]{1,29}";
    private static final int MAX_HOURS_DEBT = 40;
    private static final int MIN_HOURS_DEBT = 40;

    @Autowired
    StudentDao studentDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    RoomDao roomDao;
    @Autowired
    FacultyDao facultyDao;
    @Autowired
    EquipmentDao equipmentDao;
    @Autowired
    TaskDao taskDao;
    @Autowired
    RoomService roomService;
    @Autowired
    GroupService groupService;


    public BigInteger insert(Student student) throws ValidationException {
        ValidationsUtils.validateName(student.getFirstName(), NAME_PATTERN);
        ValidationsUtils.validateName(student.getLastName(), NAME_PATTERN);
        ValidationsUtils.validaHoursDebt(student.getHoursDebt(), MAX_HOURS_DEBT, MIN_HOURS_DEBT);

        return studentDao.insert(student);
    }

    public StudentDTO getById(BigInteger id) throws ValidationException {
        ValidationsUtils.validateId(id);
        Student student = studentDao.getById(id);

        return getDTO(student);
    }

    public List<StudentDTO> getAll(long limit, long offset) {
        List<Student> students = studentDao.getAll(limit, offset);
        return getDTOS(students);
    }

    public List<StudentDTO> getAllByFloor(BigInteger floorId) throws ValidationException {
        ValidationsUtils.validateId(floorId);
        List<Student> students = studentDao.getAllByFloor(floorId);

        return getDTOS(students);
    }

    public List<StudentDTO> getAllByFaculty(BigInteger facultyId) throws ValidationException {
        ValidationsUtils.validateId(facultyId);
        List<Student> students = studentDao.getAllByFaculty(facultyId);

        return getDTOS(students);
    }

    public List<StudentDTO> getAllByCourse(BigInteger courseId) throws ValidationException {
        ValidationsUtils.validateId(courseId);
        List<Student> students = studentDao.getAllByCourse(courseId);

        return getDTOS(students);
    }

    public List<StudentDTO> getAllWithDebitByGroup(BigInteger groupId, int hoursDebt) throws ValidationException {
        ValidationsUtils.validateId(groupId);
        List<Student> students = studentDao.getAllWithDebitByGroup(groupId, hoursDebt);

        return getDTOS(students);
    }

    public boolean changeRoom(BigInteger newRoomId, BigInteger studentId) {
        return studentDao.changeRoom(newRoomId, studentId);
    }

    public boolean changeDebt(int newHoursDebt, BigInteger studentId) {
        return studentDao.changeDebt(newHoursDebt, studentId);
    }

    public boolean update(Student student) throws ValidationException {
        ValidationsUtils.validateName(student.getFirstName(), NAME_PATTERN);
        ValidationsUtils.validateName(student.getLastName(), NAME_PATTERN);
        ValidationsUtils.validaHoursDebt(student.getHoursDebt(), MAX_HOURS_DEBT, MIN_HOURS_DEBT);

        return studentDao.update(student);
    }

    public boolean deleteById(BigInteger id) {
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
}
