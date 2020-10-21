package ua.com.foxminded.studenthostel.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.dto.GroupDTO;
import ua.com.foxminded.studenthostel.service.GroupService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class GroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);
    private static final long LINES_LIMIT_ON_PAGE = 10;

    @Autowired
    private GroupService groupService;

    @GetMapping("/groups/insert")
    public String insert(Model model) {

        model.addAttribute("group", new Group());
        return "groups/group-insert";
    }

    @PostMapping("/groups/insert")
    public String insert(Group group, Model model) {

        BigInteger id = groupService.insert(group);

        model.addAttribute("message", "Adding completed.");
        model.addAttribute("id", "New ID = " + id);

        return "message";
    }

    @GetMapping("/groups/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("getting by id: {}", id);

        GroupDTO groupDTO = groupService.getDTOById(BigInteger.valueOf(id));
        model.addAttribute("groupDTO", groupDTO);

        LOGGER.debug("getting complete: {}", groupDTO);
        return "groups/group-info";
    }

    @GetMapping("/groups/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {
        LOGGER.debug("getting all, page number: {}", pageNumber);

        long offset = LINES_LIMIT_ON_PAGE * pageNumber - LINES_LIMIT_ON_PAGE;
        LOGGER.debug("getting all, limit {} , offset {} ", LINES_LIMIT_ON_PAGE, offset);

        List<Group> groups = groupService.getAll(LINES_LIMIT_ON_PAGE, offset);
        model.addAttribute("groups", groups);

        LOGGER.debug("getting complete, page number: {}, result size: {}", pageNumber, groups.size());
        return "groups/groups-list";
    }

    @GetMapping("/groups/update/{id}")
    public String update(@PathVariable long id, Model model) {

        Group group = groupService.getById(BigInteger.valueOf(id));
        model.addAttribute("group", group);
        return "groups/group-update";
    }

    @PostMapping("/groups/update/{id}")
    public String update(@PathVariable long id, Model model,
                         Group group) {

        groupService.update(group);

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", "Updated ID = " + group.getId());

        return "message";
    }

    @PostMapping("/groups/{id}")
    public String delete(@PathVariable long id, Model model) {
        groupService.deleteById(BigInteger.valueOf(id));

        model.addAttribute("message", "Deleting complete");
        model.addAttribute("id", "Deleted group id = " + id);

        return "message";
    }
}
