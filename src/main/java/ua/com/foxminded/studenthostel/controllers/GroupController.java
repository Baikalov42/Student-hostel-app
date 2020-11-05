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
import ua.com.foxminded.studenthostel.service.GroupService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class GroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupController.class);
    private static final int LINES_LIMIT_ON_PAGE  = 10;

    @Autowired
    private GroupService groupService;

    @GetMapping("/groups/insert")
    public String insert(Model model) {
        LOGGER.debug("(GET) insert");

        model.addAttribute("group", new Group());
        return "groups/group-insert";
    }

    @PostMapping("/groups/insert")
    public String insert(Group group, Model model) {
        LOGGER.debug("(POST) insert {}", group);

        BigInteger id = groupService.insert(group);

        model.addAttribute("message", "Adding completed.");
        model.addAttribute("id", "New ID = " + id);

        LOGGER.debug("(POST) insert complete, model := {}", model);
        return "message";
    }

    @GetMapping("/groups/{id}")
    public String getById(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) getById: {}", id);

        Group group = groupService.getById(BigInteger.valueOf(id));
        model.addAttribute("group", group);

        LOGGER.debug("(GET) getById complete, model: {}", model);
        return "groups/group-info";
    }

    @GetMapping("/groups/page/{pageNumber}")
    public String getAll(@PathVariable int pageNumber, Model model) {
        LOGGER.debug("(GET) getAll, page number: {}", pageNumber);

        int offset = LINES_LIMIT_ON_PAGE  * pageNumber - LINES_LIMIT_ON_PAGE ;
        LOGGER.debug("(GET) getAll, offset {} , limit {} ", offset, LINES_LIMIT_ON_PAGE );

        List<Group> groups = groupService.getAll(offset, LINES_LIMIT_ON_PAGE );
        model.addAttribute("groups", groups);

        LOGGER.debug("(GET) getAll complete, page number: {}, result size: {}", pageNumber, groups.size());
        return "groups/groups-list";
    }

    @GetMapping("/groups/update/{id}")
    public String update(@PathVariable long id, Model model) {
        LOGGER.debug("(GET) update id = {}", id);

        Group group = groupService.getById(BigInteger.valueOf(id));
        model.addAttribute("group", group);

        return "groups/group-update";
    }

    @PostMapping("/groups/update/{id}")
    public String update(@PathVariable long id, Model model, Group group) {
        LOGGER.debug("(POST) update group = {}", group);

        Group updated = groupService.update(group);

        model.addAttribute("message", "Updating complete");
        model.addAttribute("id", "Updated ID = " + updated.getId());

        LOGGER.debug("(POST) update complete, model: {}", model);
        return "message";
    }

    @PostMapping("/groups/{id}")
    public String delete(@PathVariable long id, Model model) {
        LOGGER.debug("(POST) delete, id {}", id);

        groupService.deleteById(BigInteger.valueOf(id));

        model.addAttribute("message", "Deleting complete");
        model.addAttribute("id", "Deleted group id = " + id);

        LOGGER.debug("(POST) delete complete, model: {}", model);
        return "message";
    }
}
