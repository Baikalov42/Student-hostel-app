package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.Room;
import ua.com.foxminded.studenthostel.models.dto.RoomDTO;
import ua.com.foxminded.studenthostel.service.EquipmentService;
import ua.com.foxminded.studenthostel.service.RoomService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class RoomController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoomController.class);
    private static final long LINES_LIMIT_ON_PAGE = 10;

    @Autowired
    private RoomService roomService;
    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/rooms/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("getting by id: {}", id);

        RoomDTO roomDTO = roomService.getDTOById(BigInteger.valueOf(id));
        model.addAttribute("roomDTO", roomDTO);

        LOGGER.debug("getting complete: {}", roomDTO);
        return "rooms/rooms-info";
    }

    @GetMapping("/rooms/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("getting all, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("getting all, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Room> rooms = roomService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("rooms", rooms);

        LOGGER.debug("getting complete, page number: {}, result size: {}", pageNumber, rooms.size());
        return "rooms/rooms-list";
    }

    @GetMapping("/rooms/byEquipment/page/{pageNumber}")
    public String getEquipments(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("getting Equipments, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("getting Equipments, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Equipment> equipments = equipmentService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("equipments", equipments);

        LOGGER.debug("getting Equipments complete, page number: {}, result size: {}"
                , pageNumber, equipments.size());
        return "rooms/rooms-by-equipment";
    }

    @GetMapping("/rooms/byEquipment/{equipmentId}")
    public String getAllByEquipmentResult(@PathVariable long equipmentId, Model model) {
        LOGGER.debug("getting all by equipment, id: {}", equipmentId);

        List<RoomDTO> rooms = roomService.getAllByEquipment(BigInteger.valueOf(equipmentId));
        model.addAttribute("rooms", rooms);

        LOGGER.debug("getting all by equipment complete, result size: {}", rooms.size());
        return "rooms/rooms-list";
    }
}
