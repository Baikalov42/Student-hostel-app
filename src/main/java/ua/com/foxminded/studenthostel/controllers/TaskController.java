package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.service.TaskService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class TaskController {

    private static final long LIMIT = 10;

    @Autowired
    TaskService taskService;

    @GetMapping("/tasks/{id}")
    public String getById(@PathVariable long id, Model model) {

        Task task = taskService.getById(BigInteger.valueOf(id));
        model.addAttribute("task", task);

        return "tasks/getById";
    }

    @GetMapping("/tasks/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber,
                         Model model) {

        long offset = LIMIT * pageNumber - LIMIT;
        List<Task> tasks = taskService.getAll(LIMIT, offset);

        model.addAttribute("tasks", tasks);

        return "tasks/getAll";
    }
    @GetMapping("/tasks/byStudent/{studentId}")
    public String getAllByStudent(@PathVariable long studentId, Model model) {

        List<Task> tasks = taskService.getAllByStudent(BigInteger.valueOf(studentId));
        model.addAttribute("tasks", tasks);

        return "tasks/getAll";
    }
}
