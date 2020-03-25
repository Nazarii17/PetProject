package com.tkachuk.pet.controller;


import com.tkachuk.pet.service.OrganizationService;
import com.tkachuk.pet.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Value("${upload.path}")
    private String uploadPath;
    //Todo logger
    //    private final Logger logger = LoggerFactory.getLogger(PhotoController.class);
    private final OrganizationService organizationService;
    private final PhotoService photoService;

    @Autowired
    public PhotoController(OrganizationService organizationService, PhotoService photoService) {
        this.organizationService = organizationService;
        this.photoService = photoService;
    }

    @GetMapping("/all/{id}")
    public String getAllPhotosOfOrganization(@PathVariable Long id,
                                             Model model) {
        model.addAttribute("organizationPhotos", organizationService.getOne(id).getOrganizationPhotos());
        return "organizationPhotos";
    }

    //Todo Should it put it to OrgController?
    @PostMapping("/all/{id}")
    public String addPhotos(@PathVariable Long id,
                            @RequestParam("organizationPhotos") MultipartFile[] photos
    ) throws IOException {
        organizationService.addNewPhotos(id, photos);
        return "redirect:/photos/all/{id}";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id,
                         @RequestParam("file") MultipartFile logo,
                         @RequestParam(value = "organizationId") Long orgId
    ) throws IOException {
        photoService.update(id, logo);
        return "redirect:/photos/all/" + orgId;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id,
                         @RequestParam(value = "organizationId") Long orgId) {
        photoService.delete(id);
        return "redirect:/photos/all/" + orgId;
    }
}
