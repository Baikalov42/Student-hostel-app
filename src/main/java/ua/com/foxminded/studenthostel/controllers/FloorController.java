package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.service.FloorService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class FloorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FloorController.class);
    private static final long LINES_LIMIT_ON_PAGE = 10;

    @Autowired
    private FloorService floorService;

    @GetMapping("/floors/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("getting by id: {}", id);

        Floor floor = floorService.getById(BigInteger.valueOf(id));
        model.addAttribute("floor", floor);

        LOGGER.debug("getting complete: {}", floor);
        return "floors/floor-info";
    }

    @GetMapping("/floors/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("getting all, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("getting all, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Floor> floors = floorService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("floors", floors);

        LOGGER.debug("getting complete, page number: {}, result size: {}", pageNumber, floors.size());
        return "floors/floors-list";
    }
}
