package com.tkachuk.pet.controller;

import com.tkachuk.pet.dto.UserDto;
import com.tkachuk.pet.dto.UserProfileDto;
import com.tkachuk.pet.entity.Gender;
import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String getUserProfile(@AuthenticationPrincipal User user,
                                 Model model) {
        model.addAttribute("user", userService.findDtoById(user.getId()));
        return "profile";
    }

    @PostMapping("/profile/change-username/{id}")
    public String updateUsername(@PathVariable("id") Long id,
                                 @Valid UserDto user,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.findDtoById(id));
            return "profile";
        } else {
            userService.updateUsername(id, user);
            return "redirect:/users/profile";
        }
    }

    @PostMapping("/profile/change-photo/{id}")
    public String updateProfilePhoto(@PathVariable("id") Long id,
                                     @RequestParam("file") MultipartFile photo) {
        userService.changeProfilePhoto(id, photo);
        return "redirect:/users/profile";
    }

    @GetMapping("/profile/edit/{id}")
    public String getUserProfileEditForm(@PathVariable("id") Long id,
                                         UserProfileDto userProfileDto,
                                         Model model) {
        model.addAttribute("user", userService.findDtoById(id));
        model.addAttribute("genders", Gender.values());
        return "userProfileEdit";
    }

    @PostMapping("/profile/edit/{id}")
    public String editUserProfile(@PathVariable("id") Long id,
                                  @Valid UserProfileDto userProfileDto,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.findDtoById(id));
            model.addAttribute("genders", Gender.values());
            return "userProfileEdit";
        } else {
            userService.updateByAdmin(userProfileDto, id);
        }
        return "redirect:/users/profile";
    }

    @GetMapping("/photos/all")
    public String getAllPhotosOfUser(@AuthenticationPrincipal User user,
                                     Model model) {
        model.addAttribute("userPhotos", userService.findAllPhotosByUserId(user.getId()));
        return "userPhotos";
    }

    @PostMapping("/photos/all")
    public String addUserPhotos(@AuthenticationPrincipal User user,
                                @RequestParam("userPhotos") MultipartFile[] photos) {
        userService.addNewPhotos(user.getId(), photos);
        return "redirect:/users/photos/all";
    }

    @PostMapping("/photos/update/{id}")
    public String userPhotoUpdate(@PathVariable("id") Long id,
                                  @RequestParam("file") MultipartFile logo) {
        userService.updateUserPhoto(id, logo);
        return "redirect:/users/photos/all";
    }

    @GetMapping("/photos/delete/{id}")
    public String userPhotoDelete(@PathVariable("id") long id) {
        userService.deleteUserPhoto(id);
        return "redirect:/users/photos/all";
    }
}
