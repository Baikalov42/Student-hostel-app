package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.models.Group;
import ua.com.foxminded.studenthostel.models.dto.GroupDTO;
import ua.com.foxminded.studenthostel.service.GroupService;

import java.math.BigInteger;
import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private static final long LIMIT = 10;

    @Autowired
    GroupService groupService;

    @GetMapping("/getById")
    public String getById(@RequestParam long id, Model model) {

        GroupDTO groupDTO = groupService.getDTOById(BigInteger.valueOf(id));
        model.addAttribute("groupDTO", groupDTO);

        return "groups/getById";
    }

    @GetMapping("/getAll")
    public String getAll(@RequestParam(defaultValue = "1") long page,
                         Model model) {

        long offset = LIMIT * page - LIMIT;
        List<Group> groups = groupService.getAll(LIMIT, offset);

        model.addAttribute("groups", groups);

        return "groups/getAll";
    }
}
