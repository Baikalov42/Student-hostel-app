package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.service.EquipmentService;

import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping("equipments")
public class EquipmentController {

    private static final long LIMIT = 10;

    @Autowired
    EquipmentService equipmentService;

    @GetMapping("/getById")
    public String getById(@RequestParam long id, Model model) {

        Equipment equipment = equipmentService.getById(BigInteger.valueOf(id));
        model.addAttribute("equipment", equipment);

        return "equipments/getById";
    }

    @GetMapping("/getAll")
    public String getAll(@RequestParam(defaultValue = "1") long page,
                          Model model) {

        long offset = LIMIT * page - LIMIT;
        List<Equipment> equipments = equipmentService.getAll(LIMIT, offset);

        model.addAttribute("equipments", equipments);

        return "equipments/getAll";
    }

    @GetMapping("/getAllByStudent")
    public String getAllByStudent(@RequestParam long studentId,
                                  Model model) {

        List<Equipment> equipments = equipmentService.getAllByStudent(BigInteger.valueOf(studentId));
        model.addAttribute("equipments", equipments);

        return "equipments/getAll";
    }
}
