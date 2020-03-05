package com.tkachuk.pet.controllers;

import com.tkachuk.pet.entities.Organization;
import com.tkachuk.pet.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final OrganizationService organizationService;

    @Autowired
    public RegistrationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }


    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid Organization organization){
        return null;
    }
}
