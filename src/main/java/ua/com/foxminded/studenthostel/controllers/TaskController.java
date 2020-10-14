package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.models.Task;
import ua.com.foxminded.studenthostel.service.TaskService;

import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private static final long LIMIT = 10;

    @Autowired
    TaskService taskService;

    @GetMapping("/getById")
    public String getById(@RequestParam long id, Model model) {

        Task task = taskService.getById(BigInteger.valueOf(id));
        model.addAttribute("task", task);

        return "tasks/getById";
    }

    @GetMapping("/getAll")
    public String getAll(@RequestParam(defaultValue = "1") long page,
                         Model model) {

        long offset = LIMIT * page - LIMIT;
        List<Task> tasks = taskService.getAll(LIMIT, offset);

        model.addAttribute("tasks", tasks);

        return "tasks/getAll";
    }
    @GetMapping("/getAllByStudent")
    public String getAllByStudent(@RequestParam long studentId,
                         Model model) {

        List<Task> tasks = taskService.getAllByStudent(BigInteger.valueOf(studentId));
        model.addAttribute("tasks", tasks);

        return "tasks/getAll";
    }
}
