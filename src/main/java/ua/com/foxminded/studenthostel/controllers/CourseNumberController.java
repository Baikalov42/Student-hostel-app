package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.service.CourseNumberService;


import java.math.BigInteger;
import java.util.List;

@Controller
public class CourseNumberController {
    private static final long LIMIT = 10;

    @Autowired
    private CourseNumberService courseNumberService;

    @GetMapping("/courseNumbers/{id}")
    public String getById(@PathVariable long id, Model model) {

        CourseNumber courseNumber = courseNumberService.getById(BigInteger.valueOf(id));
        model.addAttribute("courseNumber", courseNumber);

        return "courseNumbers/getById";
    }

    @GetMapping("/courseNumbers/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {

        long offset = LIMIT * pageNumber - LIMIT;
        List<CourseNumber> courseNumbers = courseNumberService.getAll(LIMIT, offset);

        model.addAttribute("courses", courseNumbers);

        return "courseNumbers/getAll";
    }

    @GetMapping("/insert")
    public String insert(Model model) {

        model.addAttribute("courseNumber", new CourseNumber());
        return "courseNumbers/insert";
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute CourseNumber courseNumber,
                         Model model) {
        BigInteger id = null;

        try {
            id = courseNumberService.insert(courseNumber);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        model.addAttribute("newID", id);

        return "courseNumbers/insertResult";
    }
}
