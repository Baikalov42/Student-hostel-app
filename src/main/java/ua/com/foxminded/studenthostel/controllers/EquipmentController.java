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
import ua.com.foxminded.studenthostel.models.dto.PairDTO;
import ua.com.foxminded.studenthostel.service.EquipmentService;
import ua.com.foxminded.studenthostel.service.StudentService;

import java.math.BigInteger;
import java.util.List;

@Controller

public class EquipmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentController.class);

    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private StudentService studentService;

    @GetMapping("/equipments/insert")
    public String insert(Model model) {
        LOGGER.debug("(GET) insert");

        model.addAttribute("equipment", new Equipment());
        return "equipments/equipment-insert";
    }

    @PostMapping("/equipments/insert")
    public String insert(Equipment equipment, Model model) {
        LOGGER.debug("(POST) insert {}", equipment);

        BigInteger id = equipmentService.insert(equipment);

        model.addAttribute("message", "Adding completed.");
        model.addAttribute("id", "New ID = " + id);

        LOGGER.debug("(POST) insert complete, model := {}", model);
        return "message";
    }

    @GetMapping("/equipments/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) getById: {}", id);

        Equipment equipment = equipmentService.getById(BigInteger.valueOf(id));
        model.addAttribute("equipment", equipment);

        LOGGER.debug("(GET) getById complete, model: {}", model);
        return "equipments/equipment-info";
    }

    @GetMapping("/equipments/page/{pageNumber}")
    public String getAll(@PathVariable int pageNumber, Model model) {
        LOGGER.debug("(GET) getAll, page number: {}", pageNumber);

        List<Equipment> equipments = equipmentService.getAll(pageNumber);
        model.addAttribute("equipments", equipments);

        LOGGER.debug("(GET) getAll complete, page number: {}, result size: {}", pageNumber, equipments.size());
        return "equipments/equipments-list";
    }

    @GetMapping("/equipments/byStudent/{studentId}")
    public String getAllByStudent(@PathVariable long studentId, Model model) {
        LOGGER.debug("(GET) getAllByStudent, id: {}", studentId);

        List<Equipment> equipments = equipmentService.getAllByStudent(BigInteger.valueOf(studentId));
        model.addAttribute("equipments", equipments);

        LOGGER.debug("(GET) getAllByStudent complete, result size: {}", equipments.size());
        return "equipments/equipments-by-student";
    }

    @GetMapping("/equipments/assign/{studentId}")
    public String assignToStudent(@PathVariable BigInteger studentId, Model model) {
        LOGGER.debug("(GET) assignToStudent, student id {}", studentId);

        Student student = studentService.getById(studentId);
        PairDTO pair = new PairDTO();
        pair.setStudentId(studentId);

        model.addAttribute("student", student);
        model.addAttribute("pair", pair);

        return "equipments/equipment-assign-to-student";

    }

    @PostMapping("/equipments/assign")
    public String assignToStudent(PairDTO pair, Model model) {
        LOGGER.debug("(POST) assignToStudent, student id {}, equipment id {}", pair.getStudentId(), pair.getSecondId());

        equipmentService.assignToStudent(pair.getStudentId(), pair.getSecondId());
        String message = "Student ID = " + pair.getStudentId() + ", Equipment ID = " + pair.getSecondId();

        model.addAttribute("message", "Assigning success");
        model.addAttribute("id", message);

        LOGGER.debug("(POST) assignToStudent complete, model: {}", model);
        return "message";
    }

    @PostMapping("/equipments/unassign/{studentId}/{equipmentId}")
    public String unassignFromStudent(@PathVariable BigInteger studentId,
                                      @PathVariable BigInteger equipmentId,
                                      Model model) {
        LOGGER.debug("(POST) unassignFromStudent, student id {}, equipment id {}", studentId, equipmentId);

        equipmentService.unassignFromStudent(studentId, equipmentId);
        String message = "Equipment ID = " + equipmentId + " un assigned from Student ID = " + studentId;

        model.addAttribute("message", "Un assigning complete");
        model.addAttribute("id", message);

        LOGGER.debug("(POST) unassignFromStudent complete, model: {}", model);
        return "message";
    }

    @GetMapping("/equipments/update/{id}")
    public String update(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) update id = {}", id);

        Equipment equipment = equipmentService.getById(BigInteger.valueOf(id));
        model.addAttribute("equipment", equipment);

        return "equipments/equipment-update";
    }

    @PostMapping("/equipments/update/{id}")
    public String update(@PathVariable long id, Model model, Equipment equipment) {
        LOGGER.debug("(POST) update id = {}", id);

        Equipment updated = equipmentService.update(equipment);

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", "Updated ID = " + updated.getId());

        LOGGER.debug("(POST) update complete, model: {}", model);
        return "message";
    }

    @PostMapping("/equipments/{id}")
    public String delete(@PathVariable long id, Model model) {
        LOGGER.debug("(POST) delete, id {}", id);

        equipmentService.deleteById(BigInteger.valueOf(id));

        model.addAttribute("message", "Deleting complete");
        model.addAttribute("id", "Deleted equipment id = " + id);

        LOGGER.debug("(POST) delete complete, model: {}", model);
        return "message";
    }
}
