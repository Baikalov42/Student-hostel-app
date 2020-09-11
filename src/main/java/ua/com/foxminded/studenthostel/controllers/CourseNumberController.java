package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.service.CourseNumberService;


import java.math.BigInteger;

@Controller
public class CourseNumberController {

    @Autowired
    private CourseNumberService courseNumberService;

    @GetMapping("/findCourseNumber")
    public String getCourseNumberById(@RequestParam("id") long id, Model model) {
        CourseNumber courseNumber = courseNumberService.getById(BigInteger.valueOf(id));

  /*      model.addAttribute("id", courseNumber.getId().longValue());
        model.addAttribute("name", courseNumber.getName());*/
         model.addAttribute(courseNumber);

        return "first/course-number";
    }

}
