package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.service.FloorService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class FloorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FloorController.class);

    @Autowired
    private FloorService floorService;

    @GetMapping("/floors/insert")
    public String insert(Model model) {
        LOGGER.debug("(GET) insert");

        model.addAttribute("floor", new Floor());
        return "floors/floor-insert";
    }

    @PostMapping("/floors/insert")
    public String insert(Floor floor, Model model) {
        LOGGER.debug("(POST) insert {}", floor);

        BigInteger id = floorService.insert(floor);

        model.addAttribute("message", "Adding completed.");
        model.addAttribute("id", "New ID = " + id);

        LOGGER.debug("(POST) insert complete, model := {}", model);
        return "message";
    }

    @GetMapping("/floors/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) getById: {}", id);

        Floor floor = floorService.getById(BigInteger.valueOf(id));
        model.addAttribute("floor", floor);

        LOGGER.debug("(GET) getById complete, model: {}", model);
        return "floors/floor-info";
    }

    @GetMapping("/floors/page/{pageNumber}")
    public String getAll(@PathVariable int pageNumber, Model model) {
        LOGGER.debug("(GET) getAll, page number: {}", pageNumber);

        List<Floor> floors = floorService.getAll(pageNumber);
        model.addAttribute("floors", floors);

        LOGGER.debug("(GET) getAll complete, page number: {}, result size: {}", pageNumber, floors.size());
        return "floors/floors-list";
    }

    @GetMapping("/floors/update/{id}")
    public String update(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) update id = {}", id);

        Floor floor = floorService.getById(BigInteger.valueOf(id));
        model.addAttribute("floor", floor);

        return "floors/floor-update";
    }

    @PostMapping("/floors/update/{id}")
    public String update(@PathVariable long id, Model model, Floor floor) {
        LOGGER.debug("(POST) update id = {}", id);

        Floor updated = floorService.update(floor);

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", "Updated ID = " + updated.getId());

        LOGGER.debug("(POST) update complete, model: {}", model);
        return "message";
    }

    @PostMapping("/floors/{id}")
    public String delete(@PathVariable long id, Model model) {
        LOGGER.debug("(POST) delete, id {}", id);

        floorService.deleteById(BigInteger.valueOf(id));

        model.addAttribute("message", "Deleting complete");
        model.addAttribute("id", "Deleted floor id = " + id);

        LOGGER.debug("(POST) delete complete, model: {}", model);
        return "message";
    }
}
