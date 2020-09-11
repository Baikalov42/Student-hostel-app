package ua.com.foxminded.studenthostel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.studenthostel.models.dto.StudentDTO;
import ua.com.foxminded.studenthostel.service.StudentService;

import java.math.BigInteger;

@Controller
public class StudentController {
    @Autowired
    StudentService studentService;

    @GetMapping("/getstudent")
    public String getStudentById(@RequestParam("id") long id, Model model) {
        StudentDTO studentDTO = studentService.getById(BigInteger.valueOf(id));
        model.addAttribute("name", studentDTO.getFirstName());
        model.addAttribute("lastname", studentDTO.getLastName());
        model.addAttribute("hours", studentDTO.getHoursDebt());
        model.addAttribute("studentid", studentDTO.getId());
        model.addAttribute("roomname", studentDTO.getRoomDTO().getName());
        model.addAttribute("roomid", studentDTO.getRoomDTO().getId());
        model.addAttribute("floorname", studentDTO.getRoomDTO().getFloor().getName());
        model.addAttribute("floorid", studentDTO.getRoomDTO().getFloor().getId());
        model.addAttribute("groupname", studentDTO.getGroupDTO().getName());
        model.addAttribute("groupid", studentDTO.getGroupDTO().getId());
        model.addAttribute("facultyname", studentDTO.getGroupDTO().getFaculty().getName());
        model.addAttribute("facultyid", studentDTO.getGroupDTO().getFaculty().getId());
        model.addAttribute("coursenumbername", studentDTO.getGroupDTO().getCourseNumber().getName());
        model.addAttribute("coursenumberid", studentDTO.getGroupDTO().getCourseNumber().getId());

        return "first/student";
    }
}
