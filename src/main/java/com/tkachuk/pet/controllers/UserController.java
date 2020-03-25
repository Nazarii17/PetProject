package com.tkachuk.pet.controllers;

import com.tkachuk.pet.dto.UserAdditionFormWithPasswordDto;
import com.tkachuk.pet.dto.UserDto;
import com.tkachuk.pet.entities.Role;
import com.tkachuk.pet.entities.User;
import com.tkachuk.pet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('USER')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public String userList(Model model) {
        model.addAttribute("usersCommonInfoDtoList", userService.findAllCommonInfoDto());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/edit/{id}")
    public String userEditForm(@PathVariable("id") Long id,
                               Model model) {
        model.addAttribute("userAdditionFormWithPasswordDto", userService.findUserAdditionFormWithPasswordDtoById(id));
        model.addAttribute("roles", Role.values());
        return "editUser";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/edit/{id}")
    public String save(@PathVariable("id") Long id,
                       @Valid UserAdditionFormWithPasswordDto userAdditionFormWithPasswordDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userAdditionFormWithPasswordDto", userAdditionFormWithPasswordDto);
            model.addAttribute("roles", Role.values());
            return "editUser";
        } else {
            userService.update(userAdditionFormWithPasswordDto, id);
        }
        return "redirect:/user/all";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.deleteById(id);
        return "redirect:/user/all";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user,
                             UserDto userDto,
                             Model model) {
        model.addAttribute("userProfileDto", userService.findUserProfileDto(user));
        model.addAttribute("userDto", userService.findUserDto(user));
        return "profile";
    }

    //Todo new DTO
    @PostMapping("/profile/{id}")
    public String updateProfile(@PathVariable("id") Long id,
                                @Valid User user,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return "profile";
        } else {
            userService.update(user, id);
        }
        return "redirect:/user/profile";
    }

    @PostMapping("/profile/change-username/{id}")
    public String updateName(@AuthenticationPrincipal User user,
                             @PathVariable("id") Long id,
                             @Valid UserDto userDto,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userProfileDto", userService.findUserProfileDto(user));
            return "profile";
        } else {
            userService.updateName(id, userDto);
            return "redirect:/user/profile";
        }
    }

    @PostMapping("/profile/change-photo/{id}")
    public String updateProfilePhoto(@PathVariable("id") Long id,
                                     @RequestParam("file") MultipartFile photo) throws IOException {
        userService.setProfilePhoto(id, photo);
        return "redirect:/user/profile";
    }

    @GetMapping(value = "/find-by-name", params = {"wanted-name"})
    public String findByUsernameStartsWith(@RequestParam(value = "wanted-name") String wantedName, Model model) {
        model.addAttribute("usersCommonInfoDtoList", userService.findAllCommonInfoDtoUsernameStartsWith(wantedName));
        return "userList";
    }
}
