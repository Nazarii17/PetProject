package com.tkachuk.pet.controller;


import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.service.OrganizationService;
import com.tkachuk.pet.service.PhotoService;
import com.tkachuk.pet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserService userService;
    private final PhotoService photoService;

    @Autowired
    public PhotoController(OrganizationService organizationService, UserService userService, PhotoService photoService) {
        this.organizationService = organizationService;
        this.userService = userService;
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

    @GetMapping("/user/all")
    public String getAllPhotosOfUser(@AuthenticationPrincipal User user,
                                     Model model) {
        model.addAttribute("userPhotos", userService.getPhotosOfUser(user));
        return "userPhotos";
    }

    //Todo Should it put it to UserController?
    @PostMapping("/user/all")
    public String addUserPhotos(@AuthenticationPrincipal User user,
                                @RequestParam("userPhotos") MultipartFile[] photos
    ) throws IOException {
        userService.addNewPhotos(user.getId(), photos);
        return "redirect:/photos/user/all";
    }

    @PostMapping("/user/update/{id}")
    public String userPhotoUpdate(@PathVariable("id") Long id,
                                  @RequestParam("file") MultipartFile logo
    ) throws IOException {
        photoService.updateUserPhoto(id, logo);
        return "redirect:/photos/user/all";
    }

    @GetMapping("/user/delete/{id}")
    public String userPhotoDelete(@PathVariable("id") long id) {
        photoService.deleteUserPhoto(id);
        return "redirect:/photos/user/all";
    }
}
