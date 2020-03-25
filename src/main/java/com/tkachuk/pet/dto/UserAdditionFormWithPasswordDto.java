package com.tkachuk.pet.dto;

import com.tkachuk.pet.entity.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@ToString
public class UserAdditionFormWithPasswordDto {
    private Long id;
    @NotBlank(message = "Please fill the Username!")
    private String username;
    @NotBlank(message = "Please fill the Password!")
    private String password;
    @Email(message = "Email is not correct!")
    @NotBlank(message = "Please fill the Email!")
    private String email;
    private Set<Role> roles;
}
