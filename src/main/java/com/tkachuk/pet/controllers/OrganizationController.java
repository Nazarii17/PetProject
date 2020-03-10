package com.tkachuk.pet.controllers;

import com.tkachuk.pet.dtos.OrganizationDto;
import com.tkachuk.pet.entities.Organization;
import com.tkachuk.pet.entities.OrganizationType;
import com.tkachuk.pet.entities.User;
import com.tkachuk.pet.services.OrganizationDtoService;
import com.tkachuk.pet.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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
    public String getInfo(@PathVariable String name,
                          Model model) {
        Organization organization = organizationService.findByName(name);
        model.addAttribute("organization", organization);
        model.addAttribute("types", OrganizationType.values());
        return "info";
    }

    @GetMapping("/add")
    public String save(Organization organization,
                      Model model) {
        model.addAttribute("types", OrganizationType.values());
        return "organizationAdd";
    }


    @PostMapping("/add")
    public String save(
            @AuthenticationPrincipal User user,
            @Valid Organization organization,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            return organizationService.getAdditionPageWithErrors(organization, bindingResult, model);
        } else {
            organizationService.save(user, organization, file);
            model.addAttribute("message", null);
        }
        List<OrganizationDto> organizationDtoList = organizationDtoService.findAll();
        model.addAttribute("organizationDtoList", organizationDtoList);
        return "organizationDtoList";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,
                         @Valid Organization organization,
                         @RequestParam("file") MultipartFile file,
                         Model model
    ) throws IOException {
        organizationService.update(id, organization, file);
        List<OrganizationDto> organizationDtoList = organizationDtoService.findAll();
        model.addAttribute("organizationDtoList", organizationDtoList);
        return "redirect:/organizations/all";
    }

    @GetMapping("/delete/{id}")
    public String delete (@PathVariable("id") long id,
                             Model model) {
        organizationService.delete(id);
        model.addAttribute("organizationDtoList", organizationDtoService.findAll());
        return "redirect:/organizations/all";
    }
}
