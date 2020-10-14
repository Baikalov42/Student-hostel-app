package ua.com.foxminded.studenthostel.controllers;

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

    private static final long LIMIT = 10;

    @Autowired
    FacultyService facultyService;

    @GetMapping("faculties/{id}")
    public String getById(@PathVariable long id, Model model) {

        Faculty faculty = facultyService.getById(BigInteger.valueOf(id));
        model.addAttribute("faculty", faculty);

        return "faculties/getById";
    }

    @GetMapping("/faculties/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {

        long offset = LIMIT * pageNumber - LIMIT;
        List<Faculty> faculties = facultyService.getAll(LIMIT, offset);

        model.addAttribute("faculties", faculties);

        return "faculties/getAll";
    }
}
