package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.Student;
import ua.com.foxminded.studenthostel.service.EquipmentService;
import ua.com.foxminded.studenthostel.service.StudentService;

import java.math.BigInteger;
import java.util.List;

@Controller

public class EquipmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentController.class);
    private static final long LINES_LIMIT_ON_PAGE = 10;

    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private StudentService studentService;

    @GetMapping("/equipments/insert")
    public String insert(Model model) {

        model.addAttribute("equipment", new Equipment());
        return "equipments/equipment-insert";
    }

    @PostMapping("/equipments/insert")
    public String insert(Equipment equipment, Model model) {

        BigInteger id = equipmentService.insert(equipment);

        model.addAttribute("message", "Adding completed.");
        model.addAttribute("id", "New ID = " + id);

        return "message";
    }

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
        return "equipments/equipments-by-student";
    }

    @GetMapping("/equipments/assign/{studentId}")
    public String assignToStudent(@PathVariable BigInteger studentId, Model model) {

        Student student = studentService.getById(studentId);

        model.addAttribute("student", student);

        return "equipments/equipment-assign-to-student";

    }

    @PostMapping("/equipments/assign")
    public String assignToStudent(Student student, Model model) {
        System.err.println(student);

        equipmentService.assignToStudent(student.getId(), student.getGroupId());
        String message = "Student ID = " + student.getId() + ", Equipment ID = " + student.getGroupId();

        model.addAttribute("message", "Assigning success");
        model.addAttribute("id", message);

        return "message";
    }

    @PostMapping("/equipments/unassign/{studentId}/{equipmentId}")
    public String unassignFromStudent(@PathVariable BigInteger studentId,
                                      @PathVariable BigInteger equipmentId,
                                      Model model) {

        equipmentService.unassignFromStudent(studentId, equipmentId);
        String message = "Equipment ID = " + equipmentId + " un assigned from Student ID = " + studentId;

        model.addAttribute("message", "Un assigning complete");
        model.addAttribute("id", message);

        return "message";
    }

    @GetMapping("/equipments/update/{id}")
    public String update(@PathVariable long id, Model model) {

        Equipment equipment = equipmentService.getById(BigInteger.valueOf(id));
        model.addAttribute("equipment", equipment);
        return "/equipments/equipment-update";
    }

    @PostMapping("/equipments/update/{id}")
    public String update(@PathVariable long id, Model model,
                         Equipment equipment) {

        equipmentService.update(equipment);

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", "Updated ID = " + equipment.getId());
        return "message";
    }

    @PostMapping("/equipments/{id}")
    public String delete(@PathVariable long id, Model model) {
        equipmentService.deleteById(BigInteger.valueOf(id));

        model.addAttribute("message", "Deleting complete");
        model.addAttribute("id", "Deleted id = " + id);

        return "message";
    }
}
