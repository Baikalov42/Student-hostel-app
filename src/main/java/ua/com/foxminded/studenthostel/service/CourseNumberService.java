package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.dao.CourseNumberDao;
import ua.com.foxminded.studenthostel.models.CourseNumber;

import java.math.BigInteger;

@Component
public class CourseNumberService {

    @Autowired
    private CourseNumberDao courseNumberDao;

    public CourseNumber getById(BigInteger id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        return courseNumberDao.getById(id);
    }
}
