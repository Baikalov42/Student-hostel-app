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

    @GetMapping("/rooms/insert")
    public String insert(Model model) {
        LOGGER.debug("(GET) insert");

        model.addAttribute("room", new Room());
        return "rooms/room-insert";
    }

    @PostMapping("/rooms/insert")
    public String insert(Room room, Model model) {
        LOGGER.debug("(POST) insert {}", room);

        BigInteger id = roomService.insert(room);

        model.addAttribute("message", "Adding completed.");
        model.addAttribute("id", "New ID = " + id);

        LOGGER.debug("(POST) insert complete, model := {}", model);
        return "message";
    }

    @GetMapping("/rooms/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) getById: {}", id);

        RoomDTO roomDTO = roomService.getDTOById(BigInteger.valueOf(id));
        model.addAttribute("roomDTO", roomDTO);

        LOGGER.debug("(GET) getById complete, model: {}", model);
        return "rooms/room-info";
    }

    @GetMapping("/rooms/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("(GET) getAll, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("(GET) getAll, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Room> rooms = roomService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("rooms", rooms);

        LOGGER.debug("(GET) getAll complete, page number: {}, result size: {}", pageNumber, rooms.size());
        return "rooms/rooms-list";
    }

    @GetMapping("/rooms/byEquipment/page/{pageNumber}")
    public String getAllByEquipment(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("(GET) getAllByEquipment, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("(GET) getting Equipments, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Equipment> equipments = equipmentService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("equipments", equipments);

        LOGGER.debug("(GET) getAllByEquipment complete, page number: {}, result size: {}"
                , pageNumber, equipments.size());
        return "rooms/rooms-by-equipment";
    }

    @GetMapping("/rooms/byEquipment/{equipmentId}")
    public String getAllByEquipmentResult(@PathVariable long equipmentId, Model model) {
        LOGGER.debug("(GET) getAllByEquipmentResult , id: {}", equipmentId);

        List<RoomDTO> rooms = roomService.getAllByEquipment(BigInteger.valueOf(equipmentId));
        model.addAttribute("rooms", rooms);

        LOGGER.debug("(GET) getAllByEquipmentResult complete, result size: {}", rooms.size());
        return "rooms/rooms-list";
    }

    @GetMapping("/rooms/update/{id}")
    public String update(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) update id = {}", id);

        Room room = roomService.getById(BigInteger.valueOf(id));
        model.addAttribute("room", room);

        return "rooms/room-update";
    }

    @PostMapping("/rooms/update/{id}")
    public String update(@PathVariable long id, Model model, Room room) {
        LOGGER.debug("(POST) update id = {}", id);

        roomService.update(room);

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", "Updated ID = " + room.getId());

        LOGGER.debug("(POST) update complete, model: {}", model);
        return "message";
    }

    @PostMapping("/rooms/{id}")
    public String delete(@PathVariable long id, Model model) {
        LOGGER.debug("(POST) delete, id {}", id);

        roomService.deleteById(BigInteger.valueOf(id));

        model.addAttribute("message", "Deleting complete");
        model.addAttribute("id", "Deleted room id = " + id);

        LOGGER.debug("(POST) delete complete, model: {}", model);
        return "message";
    }
}
