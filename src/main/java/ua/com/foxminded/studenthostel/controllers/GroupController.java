package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.dto.GroupDTO;
import ua.com.foxminded.studenthostel.service.GroupService;

import java.math.BigInteger;
import java.util.List;

@Controller
public class GroupController {

    private static final long LIMIT = 10;

    @Autowired
    GroupService groupService;

    @GetMapping("/groups/{id}")
    public String getById(@PathVariable long id, Model model) {

        GroupDTO groupDTO = groupService.getDTOById(BigInteger.valueOf(id));
        model.addAttribute("groupDTO", groupDTO);

        return "groups/getById";
    }

    @GetMapping("/groups/page/{pageNumber}")
    public String getAll(@PathVariable long pageNumber, Model model) {

        long offset = LIMIT * pageNumber - LIMIT;
        List<Group> groups = groupService.getAll(LIMIT, offset);

        model.addAttribute("groups", groups);

        return "groups/getAll";
    }
}
