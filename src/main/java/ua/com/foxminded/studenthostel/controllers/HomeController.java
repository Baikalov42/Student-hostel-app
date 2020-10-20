package ua.com.foxminded.studenthostel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/")
    public String helloPage() {
        return "home";
    }
}
