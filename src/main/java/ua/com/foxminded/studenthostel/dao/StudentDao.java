package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.Student;

import java.math.BigInteger;
import java.util.List;

public interface StudentDao {
    BigInteger insert(Student student);

    Student getById(BigInteger studentId);

    List<Student> getAll(long limit, long offset);

    List<Student> getAllByFloor(BigInteger floorId);

    List<Student> getAllByFaculty(BigInteger facultyId);

    List<Student> getAllByCourse(BigInteger courseId);

    List<Student> getAllWithDebitByGroup(BigInteger groupId, int numberOfHoursDebt);

    boolean changeRoom(BigInteger newRoomId, BigInteger studentId);

    boolean changeDebt(int newHoursDebt, BigInteger studentId);

    Integer getStudentsCountByRoom(BigInteger roomID);

    boolean update(Student student);

    boolean deleteById(BigInteger id);
}
