package com.tkachuk.pet.controller;

import com.tkachuk.pet.dto.UserAdditionFormWithPasswordDto;
import com.tkachuk.pet.dto.UserDto;
import com.tkachuk.pet.dto.UserProfileDto;
import com.tkachuk.pet.entity.Gender;
import com.tkachuk.pet.entity.Role;
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
    public String getUsersCommonInfoDtoList(Model model) {
        model.addAttribute("usersCommonInfoDtoList", userService.findAllCommonInfoDto());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/edit/{id}")
    public String getUserEditForm(@PathVariable("id") Long id,
                                  Model model) {
        model.addAttribute("userAdditionFormWithPasswordDto", userService.getOneUserAdditionFormWithPasswordDtoById(id));
        model.addAttribute("roles", Role.values());
        return "editUser";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/edit/{id}")
    public String saveByAdministration(@PathVariable("id") Long id,
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
    public String getUserProfile(@AuthenticationPrincipal User user,
                                 Model model) {
        model.addAttribute("userProfileDto", userService.getOneUserProfileDto(user.getId()));
        model.addAttribute("userDto", userService.getOneUserDto(user.getId()));
        return "profile";
    }

    @PostMapping("/profile/change-username/{id}")
    public String updateUsername(@PathVariable("id") Long id,
                                 @Valid UserDto userDto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userProfileDto", userService.getOneUserProfileDto(id));
            return "profile";
        } else {
            userService.updateUsername(id, userDto);
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
        model.addAttribute("usersCommonInfoDtoList", userService.findAllCommonInfoDtoByUsernameStartsWith(wantedName));
        return "userList";
    }

    @GetMapping("/profile/edit/{id}")
    public String getUserProfileEditForm(@PathVariable("id") Long id,
                                         @Valid UserProfileDto userProfileDto,
                                         BindingResult bindingResult,
                                         Model model) {
        model.addAttribute("userProfileDto", userService.getOneUserProfileDto(id));
        model.addAttribute("genders", Gender.values());
        return "userProfileEdit";
    }

    @PostMapping("/profile/edit/{id}")
    public String editUserProfile(@PathVariable("id") Long id,
                                  @Valid UserProfileDto userProfileDto,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userProfileDto", userService.getOneUserProfileDto(id));
            model.addAttribute("genders", Gender.values());
            return "userProfileEdit";
        } else {
            userService.update(userProfileDto, id);
        }
        return "redirect:/user/profile";
    }

}
