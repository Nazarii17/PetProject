package com.tkachuk.pet.controllers;

import com.tkachuk.pet.dtos.organization.OrganizationDto;
import com.tkachuk.pet.entities.OrganizationType;
import com.tkachuk.pet.entities.User;
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

@Controller
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @RequestMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("organizationCommonInfoDtoList", organizationService.findAllCommonInfoDto());
        return "organizationsCommonInfo";
    }

    @GetMapping("/add")
    public String save(OrganizationDto organizationDto,
                       Model model) {
        model.addAttribute("types", OrganizationType.values());
        return "organizationAdd";
    }

    @PostMapping("/add")
    public String save(@AuthenticationPrincipal User user,//todo User/userDto?
                       @Valid OrganizationDto organizationDto,
                       BindingResult bindingResult,
                       Model model,
                       @RequestParam("file") MultipartFile logo

    ) throws IOException {
        if (bindingResult.hasErrors()) {
            return organizationService.getAdditionPageWithErrors(
                    organizationDto,
                    bindingResult,
                    model);
        } else {
            organizationService.save(user, organizationDto, logo);
            model.addAttribute("message", null);
        }
        return "redirect:/organizations/all";
    }

    @GetMapping("/info/{id}")
    public String getInfo(@PathVariable Long id,
                          Model model) {
        model.addAttribute("organizationDto", organizationService.findDtoById(id));
        model.addAttribute("types", OrganizationType.values());
        return "info";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,
                         @AuthenticationPrincipal User user,
                         @Valid OrganizationDto organizationDto,
                         BindingResult bindingResult,
                         @RequestParam("file") MultipartFile logo,
                         Model model
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            return organizationService.getInfoPageWithErrors(
                    organizationDto,
                    bindingResult,
                    model);
        } else {
            organizationService.update(id, user, organizationDto, logo);
            return "redirect:/organizations/all";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        organizationService.delete(id);
        return "redirect:/organizations/all";
    }
}
