package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.service.StudentService;
import ua.com.foxminded.studenthostel.service.TaskService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private static final long LINES_LIMIT_ON_PAGE = 10;

    @Autowired
    private TaskService taskService;
    @Autowired
    private StudentService studentService;

    @GetMapping("/tasks/insert")
    public String insert(Model model) {
        LOGGER.debug("(GET) insert");

        model.addAttribute("task", new Task());
        return "tasks/task-insert";
    }

    @PostMapping("/tasks/insert")
    public String insert(Task task, Model model) {
        LOGGER.debug("(POST) insert {}", task);

        BigInteger id = taskService.insert(task);

        model.addAttribute("message", "Adding completed.");
        model.addAttribute("id", "New ID = " + id);

        LOGGER.debug("(POST) insert complete, model := {}", model);
        return "message";
    }

    @GetMapping("/tasks/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) getById: {}", id);

        Task task = taskService.getById(BigInteger.valueOf(id));
        model.addAttribute("task", task);

        LOGGER.debug("(GET) getById complete, model: {}", model);
        return "tasks/task-info";
    }

    @GetMapping("/tasks/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("(GET) getAll, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("(GET) getAll, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Task> tasks = taskService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("tasks", tasks);

        LOGGER.debug("(GET) getAll complete, page number: {}, result size: {}", pageNumber, tasks.size());
        return "tasks/tasks-list";
    }

    @GetMapping("/tasks/byStudent/{studentId}")
    public String getAllByStudent(@PathVariable long studentId, Model model) {
        LOGGER.debug("(GET) getAllByStudent, id: {}", studentId);

        List<Task> tasks = taskService.getAllByStudent(BigInteger.valueOf(studentId));
        model.addAttribute("tasks", tasks);

        LOGGER.debug("(GET) getAllByStudent complete, result size: {}", tasks.size());
        return "tasks/tasks-by-student";
    }

    @GetMapping("/tasks/assign/{studentId}")
    public String assignToStudent(@PathVariable BigInteger studentId, Model model) {
        LOGGER.debug("(GET) assignToStudent, student id {}", studentId);

        Student student = studentService.getById(studentId);
        model.addAttribute("student", student);

        return "tasks/task-assign-to-student";
    }


    @PostMapping("/tasks/assign")
    public String assignToStudent(Student student, Model model) {
        LOGGER.debug("(POST) assignToStudent, student id {}, task id {}", student.getId(), student.getGroupId());

        taskService.assignToStudent(student.getId(), student.getGroupId());

        model.addAttribute("message", "Assigning success");
        model.addAttribute("id", "Student ID = " + student.getId() + ", Task ID = " + student.getGroupId());

        LOGGER.debug("(POST) assignToStudent complete, model: {}", model);
        return "message";
    }

    @PostMapping("/tasks/unassign/{studentId}/{taskId}")
    public String unassignFromStudent(@PathVariable BigInteger studentId,
                                      @PathVariable BigInteger taskId,
                                      Model model) {

        LOGGER.debug("(POST) unassignFromStudent, student id {}, equipment id {}", studentId, taskId);

        taskService.unassignFromStudent(studentId, taskId);

        model.addAttribute("message", "Un assigning success!");
        model.addAttribute("id", "Student ID = " + studentId + ", Task ID = " + taskId);

        LOGGER.debug("(POST) unassignFromStudent complete, model: {}", model);
        return "message";
    }

    @PostMapping("/tasks/accept/{studentId}/{taskId}")
    public String acceptTaskAndUpdateHours(@PathVariable BigInteger studentId,
                                           @PathVariable BigInteger taskId,
                                           Model model) {
        LOGGER.debug("(POST) acceptTaskAndUpdateHours, student {}, task {}", studentId, taskId);
        int newHours = studentService.acceptTaskAndUpdateHours(studentId, taskId);

        model.addAttribute("message", "Un assigning success!");
        model.addAttribute("id", "For student ID = " + studentId + " new hours count :" + newHours);

        LOGGER.debug("(POST) acceptTaskAndUpdateHours complete, model: {}", model);
        return "message";
    }

    @GetMapping("/tasks/update/{id}")
    public String update(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) update id = {}", id);

        Task task = taskService.getById(BigInteger.valueOf(id));
        model.addAttribute("task", task);
        return "tasks/task-update";
    }

    @PostMapping("/tasks/update/{id}")
    public String update(@PathVariable long id, Model model, Task task) {
        LOGGER.debug("(POST) update id = {}", id);

        taskService.update(task);

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", "Updated ID = " + task.getId());

        LOGGER.debug("(POST) update complete, model: {}", model);
        return "message";
    }

    @PostMapping("/tasks/{id}")
    public String delete(@PathVariable long id, Model model) {
        LOGGER.debug("(POST) delete, id {}", id);

        taskService.deleteById(BigInteger.valueOf(id));

        model.addAttribute("message", "Deleting complete");
        model.addAttribute("id", "Deleted task id = " + id);

        LOGGER.debug("(POST) delete complete, model: {}", model);
        return "message";
    }
}
