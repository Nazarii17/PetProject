package com.tkachuk.pet.controllers;

import com.tkachuk.pet.dtos.OrganizationDto;
import com.tkachuk.pet.entities.Organization;
import com.tkachuk.pet.services.OrganizationDtoService;
import com.tkachuk.pet.services.OrganizationService;
import com.tkachuk.pet.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/organizations")
public class OrganizationController {

    @Value("${upload.path}")
    private String uploadPath;

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
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("organization", organization);
            return "organizationAdd";
        } else {
            saveFile(organization, file);
            model.addAttribute("message", null);
            organizationService.organizationSave(organization);
        }
        List<OrganizationDto> organizationDtoList = organizationDtoService.findAll();
        model.addAttribute("organizationDtoList", organizationDtoList);
        return "organizationDtoList";
    }

    private void saveFile(
            @Valid Organization organization,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            organization.setLogo(resultFilename);
        }
    }

    @PostMapping("/update/{id}")
    public String editOrganization(
            @PathVariable("id") Long id,
            @Valid Organization organization,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Organization organizationToSave = organizationService.getOne(id);

        organizationToSave.setName(organization.getName());
        organizationToSave.setWebsite(organization.getWebsite());
        organizationToSave.setAddress(organization.getAddress());
        organizationToSave.setPhoneNumber(organization.getPhoneNumber());
        organizationToSave.setRating(organization.getRating());
        organizationToSave.setDescription(organization.getDescription());
        saveFile(organizationToSave, file);

        organizationService.organizationSave(organizationToSave);
        List<OrganizationDto> organizationDtoList = organizationDtoService.findAll();
        model.addAttribute("organizationDtoList", organizationDtoList);
        return "redirect:/organizations/all"; // TODO: 05.03.2020 WTF?
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {

        organizationService.delete(id);
        model.addAttribute("organizationDtoList", organizationDtoService.findAll());
        return "organizationDtoList";
    }
}
