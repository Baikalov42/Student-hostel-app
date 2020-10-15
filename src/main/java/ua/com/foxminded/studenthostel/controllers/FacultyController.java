package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.service.FacultyService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class FacultyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyController.class);
    private static final long LINES_LIMIT_ON_PAGE = 10;

    @Autowired
    private FacultyService facultyService;

    @GetMapping("faculties/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("getting by id: {}", id);

        Faculty faculty = facultyService.getById(BigInteger.valueOf(id));
        model.addAttribute("faculty", faculty);

        LOGGER.debug("getting complete: {}", faculty);
        return "faculties/faculty-info";
    }

    @GetMapping("/faculties/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("getting all, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("getting all, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Faculty> faculties = facultyService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("faculties", faculties);

        LOGGER.debug("getting complete, page number: {}, result size: {}", pageNumber, faculties.size());
        return "faculties/faculties-list";
    }
}
