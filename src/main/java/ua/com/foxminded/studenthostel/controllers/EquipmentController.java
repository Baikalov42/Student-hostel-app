package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.service.EquipmentService;

import java.math.BigInteger;

@Controller
public class EquipmentController {

    @Autowired
    EquipmentService equipmentService;

    @GetMapping("/findEquipment")
    public String getEquipmentById(@RequestParam("id") long id, Model model) {
        Equipment equipment = equipmentService.getById(BigInteger.valueOf(id));

        model.addAttribute("id", equipment.getId().longValue());
        model.addAttribute("name", equipment.getName());

        return "first/equipment";
    }
}
