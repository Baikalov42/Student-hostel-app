package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.service.CourseNumberService;


import java.math.BigInteger;

@Controller
public class CourseNumberController {

    @Autowired
    private CourseNumberService courseNumberService;

    @GetMapping("/findCourseNumber")
    public String getCourseNumberById(@RequestParam("id") long id, Model model) {
        CourseNumber courseNumber = null;
        try {
            courseNumber = courseNumberService.getById(BigInteger.valueOf(id));
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        model.addAttribute(courseNumber);

        return "first/course-number";
    }

    @GetMapping("/courseNumber/insert")
    public String insert(@RequestParam("name") String name, Model model) {
        CourseNumber courseNumber = new CourseNumber();
        courseNumber.setName(name);
        BigInteger id = null;
        try {
            id = courseNumberService.insert(courseNumber);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        model.addAttribute("newID", id);

        return "courseNumber/insertResult";
    }

}
