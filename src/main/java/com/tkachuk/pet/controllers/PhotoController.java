package com.tkachuk.pet.controllers;


import com.tkachuk.pet.entities.Organization;
import com.tkachuk.pet.entities.Photo;
import com.tkachuk.pet.services.OrganizationService;
import com.tkachuk.pet.services.PhotoService;
import com.tkachuk.pet.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Value("${upload.path}")
    private String uploadPath;

//    private final Logger logger = LoggerFactory.getLogger(PhotoController.class);
    private final OrganizationService organizationService;
    private final PhotoService photoService;

    @Autowired
    public PhotoController(OrganizationService organizationService, PhotoService photoService) {
        this.organizationService = organizationService;
        this.photoService = photoService;
    }

    @GetMapping("/add/{id}")
    public String getInfo(@PathVariable Long id,
                          Model model) {
        Organization organization = organizationService.getOne(id);
        model.addAttribute("organizationPhotos", organization.getPhotos());
        return "organizationPhotos";
    }

    @PostMapping("/add/{id}")
    public String bla( @PathVariable Long id,
                       @RequestParam("photos") MultipartFile[] photos,
                       Model model
    ) throws IOException {

        Organization organization = organizationService.getOne(id);

        Set<Photo> orgPhotos = organization.getPhotos();

        for (MultipartFile file : photos){

            Photo p = new Photo();

            if (FileUtil.isFileValid(file)) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String resultFilename = FileUtil.getFilename(file);
                FileUtil.saveFile(uploadPath, file, resultFilename);
                p.setName(resultFilename);
                orgPhotos.add(photoService.save(p));
            }
        }

        organization.setPhotos(orgPhotos);
        organizationService.save(organization);
        return "redirect:/organizations/all";
    }




}
