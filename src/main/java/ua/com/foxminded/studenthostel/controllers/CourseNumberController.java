package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.service.CourseNumberService;


import java.math.BigInteger;
import java.util.List;

@Controller
public class CourseNumberController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseNumberController.class);
    private static final long LINES_LIMIT_ON_PAGE = 10;

    @Autowired
    private CourseNumberService courseNumberService;

    @GetMapping("/courseNumbers/insert")
    public String insert(Model model) {
        LOGGER.debug("(GET) insert");

        model.addAttribute("courseNumber", new CourseNumber());
        return "courseNumbers/course-insert";
    }

    @PostMapping("/courseNumbers/insert")
    public String insert(CourseNumber courseNumber, Model model) {
        LOGGER.debug("(POST) insert {}", courseNumber);

        BigInteger id = courseNumberService.insert(courseNumber);

        model.addAttribute("message", "Adding completed.");
        model.addAttribute("id", "New ID = " + id);

        LOGGER.debug("(POST) insert complete, model := {}", model);
        return "message";
    }

    @GetMapping("/courseNumbers/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) getById: {}", id);

        CourseNumber courseNumber = courseNumberService.getById(BigInteger.valueOf(id));
        model.addAttribute("courseNumber", courseNumber);

        LOGGER.debug("(GET) getById complete, model: {}", model);
        return "courseNumbers/course-info";
    }

    @GetMapping("/courseNumbers/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("(GET) getAll, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("(GET) getAll, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<CourseNumber> courseNumbers = courseNumberService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("courses", courseNumbers);

        LOGGER.debug("(GET) getAll complete, page number: {}, result size: {}", pageNumber, courseNumbers.size());
        return "courseNumbers/courses-list";
    }

    @GetMapping("/courseNumbers/update/{id}")
    public String update(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) update id = {}", id);

        CourseNumber courseNumber = courseNumberService.getById(BigInteger.valueOf(id));
        model.addAttribute("courseNumber", courseNumber);

        return "courseNumbers/course-update";
    }

    @PostMapping("/courseNumbers/update/{id}")
    public String update(@PathVariable long id, Model model, CourseNumber courseNumber) {
        LOGGER.debug("(POST) update id = {}", id);

        courseNumberService.update(courseNumber);

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", "Updated ID = " + courseNumber.getId());

        LOGGER.debug("(POST) update complete, model: {}", model);
        return "message";
    }

    @PostMapping("/courseNumbers/{id}")
    public String delete(@PathVariable long id, Model model) {
        LOGGER.debug("(POST) delete, id {}", id);

        courseNumberService.deleteById(BigInteger.valueOf(id));

        model.addAttribute("message", "Deleting complete");
        model.addAttribute("id", "Deleted course id = " + id);

        LOGGER.debug("(POST) delete complete, model: {}", model);
        return "message";
    }
}
