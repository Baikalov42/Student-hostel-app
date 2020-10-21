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

        model.addAttribute("task", new Task());
        return "tasks/task-insert";
    }

    @PostMapping("/tasks/insert")
    public String insert(Task task, Model model) {

        BigInteger id = taskService.insert(task);

        model.addAttribute("message", "Adding completed.");
        model.addAttribute("id", "New ID = " + id);

        return "message";
    }

    @GetMapping("/tasks/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("getting by id: {}", id);

        Task task = taskService.getById(BigInteger.valueOf(id));
        model.addAttribute("task", task);

        LOGGER.debug("getting complete: {}", task);
        return "tasks/task-info";
    }

    @GetMapping("/tasks/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("getting all, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("getting all, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Task> tasks = taskService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("tasks", tasks);

        LOGGER.debug("getting complete, page number: {}, result size: {}", pageNumber, tasks.size());
        return "tasks/tasks-list";
    }

    @GetMapping("/tasks/byStudent/{studentId}")
    public String getAllByStudent(@PathVariable long studentId, Model model) {
        LOGGER.debug("getting all by student, id: {}", studentId);

        List<Task> tasks = taskService.getAllByStudent(BigInteger.valueOf(studentId));
        model.addAttribute("tasks", tasks);

        LOGGER.debug("getting all by student complete, result size: {}", tasks.size());
        return "tasks/tasks-by-student";
    }

    @GetMapping("/tasks/assign/{studentId}")
    public String assignToStudent(@PathVariable BigInteger studentId, Model model) {

        Student student = studentService.getById(studentId);
        model.addAttribute("student", student);

        return "tasks/assign-to-student";
    }


    @PostMapping("/tasks/assign")
    public String assignToStudent(Student student, Model model) {

        taskService.assignToStudent(student.getId(), student.getGroupId());

        model.addAttribute("message", "Assigning success");
        model.addAttribute("id", "Student ID = " + student.getId() + ", Task ID = " + student.getGroupId());

        return "message";
    }

    @PostMapping("/tasks/unassign/{studentId}/{taskId}")
    public String unassignFromStudent(@PathVariable BigInteger studentId,
                                      @PathVariable BigInteger taskId,
                                      Model model) {

        taskService.unassignFromStudent(studentId, taskId);

        model.addAttribute("message", "Un assigning success!");
        model.addAttribute("id", "Student ID = " + studentId + ", Task ID = " + taskId);

        return "message";
    }

    @PostMapping("/tasks/accept/{studentId}/{taskId}")
    public String acceptTaskAndUpdateHours(@PathVariable BigInteger studentId,
                                           @PathVariable BigInteger taskId,
                                           Model model) {

        int newHours = studentService.acceptTaskAndUpdateHours(studentId, taskId);

        model.addAttribute("message", "Un assigning success!");
        model.addAttribute("id", "For student ID = " + studentId + " new hours count :" + newHours);

        return "message";
    }

    @GetMapping("/tasks/update/{id}")
    public String update(@PathVariable long id, Model model) {

        Task task = taskService.getById(BigInteger.valueOf(id));
        model.addAttribute("task", task);
        return "tasks/task-update";
    }

    @PostMapping("/tasks/update/{id}")
    public String update(@PathVariable long id, Model model,
                         Task task) {

        taskService.update(task);

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", "Updated ID = " + task.getId());
        return "message";
    }

    @PostMapping("/tasks/{id}")
    public String delete(@PathVariable long id, Model model) {
        taskService.deleteById(BigInteger.valueOf(id));

        model.addAttribute("message", "Deleting complete");
        model.addAttribute("id", "Deleted task id = " + id);

        return "message";
    }
}
