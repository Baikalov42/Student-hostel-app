package ua.com.foxminded.studenthostel.dao;

import ua.com.foxminded.studenthostel.models.Student;

import java.math.BigInteger;
import java.util.List;

public interface StudentDao {
    BigInteger insert(Student student);

    Student getById(BigInteger studentId);

    List<Student> getAll(int offset, int limit);

    List<Student> getAllByFloor(BigInteger floorId);

    List<Student> getAllByFaculty(BigInteger facultyId);

    List<Student> getAllByCourse(BigInteger courseId);

    List<Student> getAllWithDebitByGroup(BigInteger groupId, int numberOfHoursDebt);

    Student changeRoom(BigInteger newRoomId, BigInteger studentId);

    Student changeDebt(int newHoursDebt, BigInteger studentId);

    Student update(Student student);

    void deleteById(BigInteger id);
}
