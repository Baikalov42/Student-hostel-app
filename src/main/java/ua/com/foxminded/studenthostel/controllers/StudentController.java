package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.Faculty;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.dto.StudentDTO;
import ua.com.foxminded.studenthostel.service.CourseNumberService;
import ua.com.foxminded.studenthostel.service.FacultyService;
import ua.com.foxminded.studenthostel.service.FloorService;
import ua.com.foxminded.studenthostel.service.StudentService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class StudentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);
    private static final long LINES_LIMIT_ON_PAGE = 10;

    @Autowired
    private StudentService studentService;
    @Autowired
    private FloorService floorService;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private CourseNumberService courseNumberService;

    @GetMapping("/students/insert")
    public String insert(Model model) {

        model.addAttribute("student", new Student());
        return "students/student-insert";
    }

    @PostMapping("/students/insert")
    public String insert(Student student, Model model) {

        BigInteger id = studentService.insert(student);
        model.addAttribute("message", "Adding completed.");
        model.addAttribute("id", "New ID = " + id);

        return "message";
    }

    @GetMapping("/students/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("getting by id: {}", id);

        StudentDTO studentDTO = studentService.getDTOById(BigInteger.valueOf(id));
        model.addAttribute("studentDTO", studentDTO);

        LOGGER.debug("getting complete: {}", studentDTO);
        return "students/student-info";
    }

    @GetMapping("/students/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("getting all, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("getting all, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Student> students = studentService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("students", students);

        LOGGER.debug("getting complete, page number: {}, result size: {}", pageNumber, students.size());
        return "students/students-list";
    }

    @GetMapping("/students/byFloor/page/{pageNumber}")
    public String getAllByFloor(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("getting Floors, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("getting Equipments, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Floor> floors = floorService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("floors", floors);


        LOGGER.debug("getting Floors complete, page number: {}, result size: {}"
                , pageNumber, floors.size());

        return "students/students-by-floor";
    }

    @GetMapping("/students/byFloor/{floorId}")
    public String getAllByFloorResult(@PathVariable long floorId, Model model) {
        LOGGER.debug("getting all by floor, id: {}", floorId);

        List<StudentDTO> students = studentService.getAllByFloor(BigInteger.valueOf(floorId));
        model.addAttribute("students", students);

        LOGGER.debug("getting all by floor complete, result size: {}", students.size());
        return "students/students-list";
    }

    @GetMapping("/students/byFaculty/page/{pageNumber}")
    public String getAllByFaculty(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("getting faculties, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("getting faculties, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Faculty> faculties = facultyService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("faculties", faculties);

        LOGGER.debug("getting faculties complete, page number: {}, result size: {}"
                , pageNumber, faculties.size());

        return "students/students-by-faculty";
    }

    @GetMapping("/students/byFaculty/{facultyId}")
    public String getAllByFacultyResult(@PathVariable long facultyId, Model model) {
        LOGGER.debug("getting all by faculty, id: {}", facultyId);

        List<StudentDTO> students = studentService.getAllByFaculty(BigInteger.valueOf(facultyId));
        model.addAttribute("students", students);

        LOGGER.debug("getting all by faculty complete, result size: {}", students.size());
        return "students/students-list";
    }

    @GetMapping("/students/byCourse/page/{pageNumber}")
    public String getAllByCourse(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("getting courses, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("getting courses, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<CourseNumber> courseNumbers = courseNumberService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("courseNumbers", courseNumbers);

        LOGGER.debug("getting courses complete, page number: {}, result size: {}"
                , pageNumber, courseNumbers.size());

        return "students/students-by-course";
    }

    @GetMapping("/students/byCourse/{courseId}")
    public String getAllByCourseResult(@PathVariable long courseId, Model model) {
        LOGGER.debug("getting all by course, id: {}", courseId);

        List<StudentDTO> students = studentService.getAllByCourse(BigInteger.valueOf(courseId));
        model.addAttribute("students", students);

        LOGGER.debug("getting all by course complete, result size: {}", students.size());
        return "students/students-list";
    }

    @GetMapping("/students/byGroupWithDebtExample")
    public String getAllByGroupWithDebtExample() {
        return "students/students-by-group-with-debt-example";
    }

    @GetMapping("/students/byGroupWithDebt/group/{groupId}/debt/{debt}")
    public String getAllByGroupWithDebt(@PathVariable long groupId,
                                        @PathVariable int debt,
                                        Model model) {

        LOGGER.debug("getting all by croup with debt, id: {}, debt: {}", groupId, debt);

        List<StudentDTO> students = studentService.getAllWithDebitByGroup(BigInteger.valueOf(groupId), debt);
        model.addAttribute("students", students);

        LOGGER.debug("getting all by croup with debt, result size: {}", students.size());
        return "students/students-list";
    }

    @GetMapping("/students/update/{id}")
    public String update(@PathVariable long id, Model model) {

        Student student = studentService.getById(BigInteger.valueOf(id));
        model.addAttribute("student", student);
        return "/students/student-update";
    }

    @PostMapping("/students/update/{id}")
    public String update(@PathVariable long id, Model model,
                         Student student) {

        studentService.update(student);
        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", "Updated ID = " + student.getId());
        return "message";
    }

    @GetMapping("/students/update-room/{roomId}")
    public String changeRoom(@PathVariable long roomId, Model model) {
        Student student = studentService.getById(BigInteger.valueOf(roomId));
        model.addAttribute("student", student);

        return "/students/student-update-room";
    }

    @PostMapping("/students/update-room")
    public String changeRoom(Student student, Model model) {

        System.out.println(student);

        studentService.changeRoom(
                BigInteger.valueOf(student.getRoomId().longValue()),
                BigInteger.valueOf(student.getId().longValue()));

        String mes = "Student ID = " + student.getId() + ", updated room on ID = " + student.getRoomId();

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", mes);
        return "message";
    }
    @GetMapping("/students/update-debt/{studentId}")
    public String changeDebt(@PathVariable long studentId, Model model) {
        Student student = studentService.getById(BigInteger.valueOf(studentId));
        model.addAttribute("student", student);

        return "/students/student-update-debt";
    }

    @PostMapping("/students/update-debt")
    public String changeDebt(Student student, Model model) {

        System.err.println(student);

        studentService.changeDebt(student.getHoursDebt(), student.getId());

        String mes = "Student ID = " + student.getId() + ", updated debt on: " + student.getHoursDebt();

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", mes);
        return "message";
    }

    @PostMapping("/students/{id}")
    public String delete(@PathVariable long id, Model model) {
        studentService.deleteById(BigInteger.valueOf(id));
        model.addAttribute("message", "Deleting complete");
        model.addAttribute("id", "Deleted id = " + id);

        return "message";
    }
}
