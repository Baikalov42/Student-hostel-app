package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/students/{id}")
    public String getById(@PathVariable long id, Model model) {

        StudentDTO studentDTO = studentService.getDTOById(BigInteger.valueOf(id));
        model.addAttribute("studentDTO", studentDTO);

        return "students/getById";
    }

    @GetMapping("/students/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber,
                         Model model) {

        long offset = LIMIT * pageNumber - LIMIT;
        List<Student> students = studentService.getAll(LIMIT, offset);

        model.addAttribute("students", students);

        return "students/getAll";
    }

    @GetMapping("/students/byFloor/page/{pageNumber}")
    public String getAllByFloor(@PathVariable long pageNumber,
                                Model model) {

        long offset = LIMIT * pageNumber - LIMIT;
        List<Floor> floors = floorService.getAll(LIMIT, offset);
        model.addAttribute("floors", floors);

        return "students/getAllByFloor";
    }

    @GetMapping("/students/byFloor/{floorId}")
    public String getAllByFloorResult(@PathVariable long floorId, Model model) {

        List<StudentDTO> students = studentService.getAllByFloor(BigInteger.valueOf(floorId));
        model.addAttribute("students", students);

        return "students/getAll";
    }

    @GetMapping("/students/byFaculty/page/{pageNumber}")
    public String getAllByFaculty(@PathVariable long pageNumber,
                                  Model model) {

        long offset = LIMIT * pageNumber - LIMIT;

        List<Faculty> faculties = facultyService.getAll(LIMIT, offset);
        model.addAttribute("faculties", faculties);

        return "students/getAllByFaculty";
    }

    @GetMapping("/students/byFaculty/{facultyId}")
    public String getAllByFacultyResult(@PathVariable long facultyId, Model model) {

        List<StudentDTO> students = studentService.getAllByFaculty(BigInteger.valueOf(facultyId));
        model.addAttribute("students", students);

        return "students/getAll";
    }

    @GetMapping("/students/byCourse/page/{pageNumber}")
    public String getAllByCourse(@PathVariable long pageNumber, Model model) {

        long offset = LIMIT * pageNumber - LIMIT;

        List<CourseNumber> courseNumbers = courseNumberService.getAll(LIMIT, offset);
        model.addAttribute("courseNumbers", courseNumbers);

        return "students/getAllByCourse";
    }

    @GetMapping("/students/byCourse/{courseId}")
    public String getAllByCourseResult(@PathVariable long courseId, Model model) {

        List<StudentDTO> students = studentService.getAllByCourse(BigInteger.valueOf(courseId));
        model.addAttribute("students", students);

        return "students/getAll";
    }

    @GetMapping("/students/byGroupWithDebtExample")
    public String getAllByGroupWithDebtExample() {

        return "students/getAllByGroupWithDebtExample";
    }

    @GetMapping("/students/byGroupWithDebt/group/{groupId}/debt/{debt}")
    public String getAllByGroupWithDebt(@PathVariable long groupId,
                                        @PathVariable int debt,
                                        Model model) {

        List<StudentDTO> students = studentService.getAllWithDebitByGroup(BigInteger.valueOf(groupId), debt);
        model.addAttribute("students", students);

        return "students/getAll";
    }
}
