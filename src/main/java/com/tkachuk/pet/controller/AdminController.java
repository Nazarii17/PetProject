package com.tkachuk.pet.controller;

import com.tkachuk.pet.dto.UserAdditionFormWithPasswordDto;
import com.tkachuk.pet.entity.Role;
import com.tkachuk.pet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/all")
    public String getUsersCommonInfoDtoList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userAll";
    }

    @GetMapping("/users/find-by-name")
    public String findByUsernameStartsWith(@RequestParam(value = "name") String wantedName, Model model) {
        model.addAttribute("users", userService.findAllByUsernameStartWith(wantedName));
        return "userAll";
    }

    @GetMapping("/users/edit/{id}")
    public String getUserEditForm(@PathVariable("id") Long id,
                                  Model model) {
        model.addAttribute("userAdditionFormWithPasswordDto", userService.getOneUserAdditionFormWithPasswordDtoById(id));
        model.addAttribute("roles", Role.values());
        return "editUser";
    }

    @PostMapping("/users/edit/{id}")
    public String saveByAdministration(@PathVariable("id") Long id,
                                       @Valid UserAdditionFormWithPasswordDto userAdditionFormWithPasswordDto,
                                       BindingResult bindingResult,
                                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userAdditionFormWithPasswordDto", userAdditionFormWithPasswordDto);
            model.addAttribute("roles", Role.values());
            return "editUser";
        } else {
            userService.updateByAdmin(userAdditionFormWithPasswordDto, id);
        }
        return "redirect:/admin/users/all";
    }

    @GetMapping("/users/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.deleteById(id);
        return "redirect:/admin/users/all";
    }

}
