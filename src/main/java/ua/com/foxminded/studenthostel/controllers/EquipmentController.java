package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.service.EquipmentService;

import java.math.BigInteger;
import java.util.List;

@Controller

public class EquipmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentController.class);
    private static final long LINES_LIMIT_ON_PAGE = 10;

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/equipments/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("getting by id: {}", id);

        Equipment equipment = equipmentService.getById(BigInteger.valueOf(id));
        model.addAttribute("equipment", equipment);

        LOGGER.debug("getting complete: {}", equipment);
        return "equipments/equipment-info";
    }

    @GetMapping("/equipments/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("getting all, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("getting all, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Equipment> equipments = equipmentService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("equipments", equipments);

        LOGGER.debug("getting complete, page number: {}, result size: {}", pageNumber, equipments.size());
        return "equipments/equipments-list";
    }

    @GetMapping("/equipments/byStudent/{studentId}")
    public String getAllByStudent(@PathVariable long studentId, Model model) {
        LOGGER.debug("getting all by student, id: {}", studentId);

        List<Equipment> equipments = equipmentService.getAllByStudent(BigInteger.valueOf(studentId));
        model.addAttribute("equipments", equipments);

        LOGGER.debug("getting complete, result size: {}", equipments.size());
        return "equipments/equipments-list";
    }
}
