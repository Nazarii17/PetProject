package com.tkachuk.pet.controllers;

import com.tkachuk.pet.entities.User;
import com.tkachuk.pet.services.OrganizationService;
import com.tkachuk.pet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class RegistrationController {

    private final UserService userService;
    private final OrganizationService organizationService;

    @Autowired
    public RegistrationController(UserService userService, OrganizationService organizationService) {
        this.userService = userService;
        this.organizationService = organizationService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        if (userService.isUserExist(user)) {
            model.put("message", "User exists!");
            return "registration";
        } else {
            userService.add(user);
            return "redirect:/login";
        }
    }
}
