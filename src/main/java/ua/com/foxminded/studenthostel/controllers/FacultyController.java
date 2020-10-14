package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.service.FacultyService;

import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping("/faculties")
public class FacultyController {

    private static final long LIMIT = 10;

    @Autowired
    FacultyService facultyService;

    @GetMapping("/getById")
    public String getById(@RequestParam long id, Model model) {

        Faculty faculty = facultyService.getById(BigInteger.valueOf(id));
        model.addAttribute("faculty", faculty);

        return "faculties/getById";
    }

    @GetMapping("/getAll")
    public String getAll(@RequestParam(defaultValue = "1") long page,
                         Model model) {

        long offset = LIMIT * page - LIMIT;
        List<Faculty> faculties = facultyService.getAll(LIMIT, offset);

        model.addAttribute("faculties", faculties);

        return "faculties/getAll";
    }
}
