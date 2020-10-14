package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.dto.RoomDTO;
import ua.com.foxminded.studenthostel.service.EquipmentService;
import ua.com.foxminded.studenthostel.service.RoomService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class RoomController {

    private static final long LIMIT = 10;

    @Autowired
    RoomService roomService;
    @Autowired
    EquipmentService equipmentService;

    @GetMapping("/rooms/{id}")
    public String getById(@PathVariable long id, Model model) {

        RoomDTO roomDTO = roomService.getDTOById(BigInteger.valueOf(id));
        model.addAttribute("roomDTO", roomDTO);

        return "rooms/getById";
    }

    @GetMapping("/rooms/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {

        long offset = LIMIT * pageNumber - LIMIT;
        List<Room> rooms = roomService.getAll(LIMIT, offset);

        model.addAttribute("rooms", rooms);

        return "rooms/getAll";
    }

    @GetMapping("/rooms/byEquipment/page/{pageNumber}")
    public String getAllByEquipment(@PathVariable long pageNumber, Model model) {

        long offset = LIMIT * pageNumber - LIMIT;
        List<Equipment> equipments = equipmentService.getAll(LIMIT, offset);
        model.addAttribute("equipments", equipments);

        return "rooms/getAllByEquipment";
    }

    @GetMapping("/rooms/byEquipment/{equipmentId}")
    public String getAllByEquipmentResult(@PathVariable long equipmentId, Model model) {

        List<RoomDTO> rooms = roomService.getAllByEquipment(BigInteger.valueOf(equipmentId));
        model.addAttribute("rooms", rooms);

        return "rooms/getAll";
    }
}
