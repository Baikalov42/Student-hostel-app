package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.service.TaskService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private static final long LINES_LIMIT_ON_PAGE = 10;

    @Autowired
    private TaskService taskService;

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
        return "tasks/tasks-list";
    }
}
