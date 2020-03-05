package com.tkachuk.pet.controllers;

import com.tkachuk.pet.dtos.OrganizationDto;
import com.tkachuk.pet.entities.Organization;
import com.tkachuk.pet.services.OrganizationDtoService;
import com.tkachuk.pet.services.OrganizationService;
import com.tkachuk.pet.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationDtoService organizationDtoService;

    @Autowired
    public OrganizationController(OrganizationService organizationService, OrganizationDtoService organizationDtoService) {
        this.organizationService = organizationService;
        this.organizationDtoService = organizationDtoService;
    }

    @RequestMapping("/all")
    public String getAll(Model model) {
        List<OrganizationDto> organizationDtoList = organizationDtoService.findAll();
        model.addAttribute("organizationDtoList", organizationDtoList);
        return "organizationDtoList";
    }

    @GetMapping("/info/{name}")
    public String getInfo(@PathVariable String name, Model model) {
        Organization organization = organizationService.findByName(name);
        model.addAttribute("organization", organization);
        return "info";
    }

    @GetMapping("/add")
    public String addOrganization(Organization organization) {
        return "organizationAdd";
    }

    @PostMapping("/add")
    public String addOrganization(
            @Valid Organization organization,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("organization", organization);
            return "organizationAdd";
        } else {
            // TODO: 04.03.2020 saveFile(message, file);
            model.addAttribute("message", null);
            organizationService.organizationSave(organization);
        }
        List<OrganizationDto> organizationDtoList = organizationDtoService.findAll();
        model.addAttribute("organizationDtoList", organizationDtoList);
        return "organizationDtoList";
    }

    @PostMapping("/update/{id}")
    public String editOrganization(
            @PathVariable("id") Long id,
            @Valid Organization organization,
            Model model
    ) {
        Organization organizationToSave = organizationService.getOne(id);

        organizationToSave.setName(organization.getName());
        organizationToSave.setWebsite(organization.getWebsite());
        organizationToSave.setAddress(organization.getAddress());
        organizationToSave.setPhoneNumber(organization.getPhoneNumber());
        organizationToSave.setRating(organization.getRating());
        organizationToSave.setDescription(organization.getDescription());

        organizationService.organizationSave(organizationToSave);
        List<OrganizationDto> organizationDtoList = organizationDtoService.findAll();
        model.addAttribute("organizationDtoList", organizationDtoList);
        return "organizationDtoList";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {

        organizationService.delete(id);
        model.addAttribute("organizationDtoList", organizationDtoService.findAll());
        return "organizationDtoList";
    }
}
