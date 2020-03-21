package com.tkachuk.pet.controllers;

import com.tkachuk.pet.dto.UserCommonInfoDto;
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

import javax.validation.Valid;

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
        model.addAttribute("userCommonInfoDto", userService.findCommonInfoDtoById(id));
        model.addAttribute("roles", Role.values());
        return "editUser";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/edit/{id}")
    public String save(@PathVariable("id") Long id,
                       @Valid UserCommonInfoDto userCommonInfoDto,
                       BindingResult bindingResult,
                       @RequestParam("password") String password,
                       Model model
    ) {
        if (bindingResult.hasErrors()) {
            return userService.getPageWithErrors("editUser" ,userCommonInfoDto, bindingResult, model);
        } else {
            userService.update(userCommonInfoDto, id, password);
        }
        return "redirect:/user/all";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.deleteById(id);
        return "redirect:/user/all";
    }

    //Todo new DTO
    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user,
                             Model model) {
        model.addAttribute("user", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    //Todo new DTO
    @PostMapping("/profile/{id}")
    public String updateProfile(@PathVariable("id") Long id,
                                @Valid User user,
                                BindingResult bindingResult,
                                Model model
    ) {
        if (bindingResult.hasErrors()) {
            return userService.getPageWithErrors("profile", user, bindingResult, model);
        } else {
            userService.update(user, id);
        }
        return "redirect:/user/profile";
    }

    @GetMapping(value = "/find-by-name",params = {"wanted-name"})
    public String findByUsernameStartsWith(@RequestParam(value = "wanted-name") String wantedName, Model model){

        model.addAttribute("usersCommonInfoDtoList", userService.findAllCommonInfoDtoUsernameStartsWith(wantedName));
        return "userList";
    }

}
