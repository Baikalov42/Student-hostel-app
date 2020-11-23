package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.service.FacultyService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class FacultyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyController.class);

    @Autowired
    private FacultyService facultyService;

    @GetMapping("/faculties/insert")
    public String insert(Model model) {
        LOGGER.debug("(GET) insert");

        model.addAttribute("faculty", new Faculty());
        return "faculties/faculty-insert";
    }

    @PostMapping("/faculties/insert")
    public String insert(Faculty faculty, Model model) {
        LOGGER.debug("(POST) insert {}", faculty);

        BigInteger id = facultyService.insert(faculty);

        model.addAttribute("message", "Adding completed.");
        model.addAttribute("id", "New ID = " + id);

        LOGGER.debug("(POST) insert complete, model := {}", model);
        return "message";
    }

    @GetMapping("faculties/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) getById: {}", id);

        Faculty faculty = facultyService.getById(BigInteger.valueOf(id));
        model.addAttribute("faculty", faculty);

        LOGGER.debug("(GET) getById complete model: {}", model);
        return "faculties/faculty-info";
    }

    @GetMapping("/faculties/page/{pageNumber}")
    public String getAll(@PathVariable int pageNumber, Model model) {
        LOGGER.debug("(GET) getAll, page number: {}", pageNumber);

        List<Faculty> faculties = facultyService.getAll(pageNumber);
        model.addAttribute("faculties", faculties);

        LOGGER.debug("(GET) getAll complete, page number: {}, result size: {}", pageNumber, faculties.size());
        return "faculties/faculties-list";
    }

    @GetMapping("/faculties/update/{id}")
    public String update(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) update id = {}", id);

        Faculty faculty = facultyService.getById(BigInteger.valueOf(id));
        model.addAttribute("faculty", faculty);

        return "faculties/faculty-update";
    }

    @PostMapping("/faculties/update/{id}")
    public String update(@PathVariable long id, Model model, Faculty faculty) {
        LOGGER.debug("(POST) update id = {}", id);

        Faculty updated = facultyService.update(faculty);

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", "Updated ID = " + updated.getId());

        LOGGER.debug("(POST) update complete, model: {}", model);
        return "message";
    }

    @PostMapping("/faculties/{id}")
    public String delete(@PathVariable long id, Model model) {
        LOGGER.debug("(POST) delete, id {}", id);

        facultyService.deleteById(BigInteger.valueOf(id));

        model.addAttribute("message", "Deleting complete");
        model.addAttribute("id", "Deleted faculty id = " + id);

        LOGGER.debug("(POST) delete complete, model: {}", model);
        return "message";
    }
}
