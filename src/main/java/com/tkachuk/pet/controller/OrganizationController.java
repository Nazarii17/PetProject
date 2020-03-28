package com.tkachuk.pet.controller;

import com.tkachuk.pet.dto.OrganizationDto;
import com.tkachuk.pet.dto.OrganizationProfileDto;
import com.tkachuk.pet.entity.OrganizationType;
import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.service.OrganizationService;
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
        model.addAttribute("organizations", organizationService.findAll());
        return "allOrganizations";
    }

    @GetMapping("/add")
    public String getAdditionForm(OrganizationDto organization,
                                  Model model) {
        model.addAttribute("organizationTypes", OrganizationType.values());
        return "addOrganization";
    }

    @PostMapping("/add")
    public String save(@AuthenticationPrincipal User user,
                       @Valid OrganizationDto organization,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("organization", organization);
            model.addAttribute("organizationTypes", OrganizationType.values());
            return "addOrganization";
        } else {
            organizationService.save(user, organization);
            return "redirect:/organizations/all";
        }
    }

    @GetMapping("/info/{id}")
    public String getInfo(@PathVariable Long id,
                          OrganizationProfileDto organization,
                          Model model) {
        model.addAttribute("organization", organizationService.findById(id));
        model.addAttribute("organizationTypes", OrganizationType.values());
        return "infoOrganization";
    }

    @PostMapping("/info/update/{id}")
    public String updateInfo(@PathVariable("id") Long id,
                             @AuthenticationPrincipal User user,
                             @Valid OrganizationProfileDto organization,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("organization", organizationService.findById(id));
            model.addAttribute("organizationTypes", OrganizationType.values());
            return "infoAboutOrganization";
        } else {
            organizationService.update(id, user, organization);
            return "redirect:/organizations/all";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        organizationService.delete(id);
        return "redirect:/organizations/all";
    }

    @PostMapping("/change-photo/{id}")
    public String updateProfilePhoto(@PathVariable("id") Long id,
                                     @RequestParam("file") MultipartFile photo) throws IOException {
        organizationService.changeLogo(id, photo);
        return "redirect:/organizations/info/{id}";
    }
}
