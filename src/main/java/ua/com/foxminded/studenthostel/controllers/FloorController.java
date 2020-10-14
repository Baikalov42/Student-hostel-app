package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.service.FloorService;

import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping("/floors")
public class FloorController {

    private static final long LIMIT = 10;

    @Autowired
    FloorService floorService;

    @GetMapping("/getById")
    public String getById(@RequestParam long id, Model model) {

        Floor floor = floorService.getById(BigInteger.valueOf(id));
        model.addAttribute("floor", floor);

        return "floors/getById";
    }

    @GetMapping("/getAll")
    public String getAll(@RequestParam(defaultValue = "1") long page,
                         Model model) {

        long offset = LIMIT * page - LIMIT;
        List<Floor> floors = floorService.getAll(LIMIT, offset);

        model.addAttribute("floors", floors);

        return "floors/getAll";
    }
}
