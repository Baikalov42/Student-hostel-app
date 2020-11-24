package ua.com.foxminded.studenthostel.repository;

import ua.com.foxminded.studenthostel.models.Student;

import java.math.BigInteger;

public interface StudentExtendedRepository {

    Student changeRoom(BigInteger newRoomId, BigInteger studentId);

    Student changeDebt(int newHoursDebt, BigInteger studentId);
}
