package com.tkachuk.pet.controller;

import com.tkachuk.pet.dto.UserDto;
import com.tkachuk.pet.dto.UserProfileDto;
import com.tkachuk.pet.entity.Gender;
import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('USER')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String getUserProfile(@AuthenticationPrincipal User user,
                                 Model model) {
        model.addAttribute("user", userService.findById(user.getId()));
        return "profile";
    }

    @PostMapping("/profile/change-username/{id}")
    public String updateUsername(@PathVariable("id") Long id,
                                 @Valid UserDto userDto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.findById(id));
            return "profile";
        } else {
            userService.updateUsername(id, userDto);
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
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("genders", Gender.values());
        return "userProfileEdit";
    }

    @PostMapping("/profile/edit/{id}")
    public String editUserProfile(@PathVariable("id") Long id,
                                  @Valid UserProfileDto userProfileDto,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.findById(id));
            model.addAttribute("genders", Gender.values());
            return "userProfileEdit";
        } else {
            userService.update(userProfileDto, id);
        }
        return "redirect:/users/profile";
    }

}
