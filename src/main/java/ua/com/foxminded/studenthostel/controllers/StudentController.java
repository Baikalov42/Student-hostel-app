package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.dto.StudentDTO;
import ua.com.foxminded.studenthostel.service.CourseNumberService;
import ua.com.foxminded.studenthostel.service.FacultyService;
import ua.com.foxminded.studenthostel.service.FloorService;
import ua.com.foxminded.studenthostel.service.GroupService;
import ua.com.foxminded.studenthostel.service.StudentService;

import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final long LIMIT = 10;


    @Autowired
    private StudentService studentService;
    @Autowired
    private FloorService floorService;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private CourseNumberService courseNumberService;
    @Autowired
    private GroupService groupService;

    @GetMapping("/getById")
    public String getById(@RequestParam long id, Model model) {

        StudentDTO studentDTO = studentService.getDTOById(BigInteger.valueOf(id));
        model.addAttribute("studentDTO", studentDTO);

        return "students/getById";
    }

    @GetMapping("/getAll")
    public String getAll(@RequestParam(defaultValue = "1") long page,
                         Model model) {

        long offset = LIMIT * page - LIMIT;
        List<Student> students = studentService.getAll(LIMIT, offset);

        model.addAttribute("students", students);

        return "students/getAll";
    }

    @GetMapping("/getAllByFloor")
    public String getAllByFloor(@RequestParam(defaultValue = "1") long page,
                                Model model) {

        long offset = LIMIT * page - LIMIT;
        List<Floor> floors = floorService.getAll(LIMIT, offset);
        model.addAttribute("floors", floors);

        return "students/getAllByFloor";
    }

    @GetMapping("/getAllByFloorResult")
    public String getAllByFloorResult(@RequestParam long floorId,
                                      Model model) {

        List<StudentDTO> students = studentService.getAllByFloor(BigInteger.valueOf(floorId));
        model.addAttribute("students", students);

        return "students/getAll";
    }

    @GetMapping("/getAllByFaculty")
    public String getAllByFaculty(@RequestParam(defaultValue = "1") long page,
                                  Model model) {

        long offset = LIMIT * page - LIMIT;

        List<Faculty> faculties = facultyService.getAll(LIMIT, offset);
        model.addAttribute("faculties", faculties);

        return "students/getAllByFaculty";
    }

    @GetMapping("/getAllByFacultyResult")
    public String getAllByFacultyResult(@RequestParam long facultyId, Model model) {

        List<StudentDTO> students = studentService.getAllByFaculty(BigInteger.valueOf(facultyId));
        model.addAttribute("students", students);

        return "students/getAll";
    }

    @GetMapping("/getAllByCourse")
    public String getAllByCourse(@RequestParam(defaultValue = "1") long page,
                                 Model model) {

        long offset = LIMIT * page - LIMIT;

        List<CourseNumber> courseNumbers = courseNumberService.getAll(LIMIT, offset);
        model.addAttribute("courseNumbers", courseNumbers);

        return "students/getAllByCourse";
    }

    @GetMapping("/getAllByCourseResult")
    public String getAllByCourseResult(@RequestParam long courseId, Model model) {

        List<StudentDTO> students = studentService.getAllByCourse(BigInteger.valueOf(courseId));
        model.addAttribute("students", students);

        return "students/getAll";
    }
    @GetMapping("/getAllByGroupWithDebtExample")
    public String getAllByGroupWithDebtExample() {

        return "students/getAllByGroupWithDebtExample";
    }

    @GetMapping("/getAllByGroupWithDebt")
    public String getAllByGroupWithDebt(@RequestParam long groupId,
                                              @RequestParam int debt,
                                              Model model) {

        List<StudentDTO> students = studentService.getAllWithDebitByGroup(BigInteger.valueOf(groupId), debt);
        model.addAttribute("students", students);

        return "students/getAll";
    }
}
