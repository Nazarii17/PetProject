package com.tkachuk.pet.dto;

import com.tkachuk.pet.entity.Gender;
import com.tkachuk.pet.entity.Role;
import com.tkachuk.pet.entity.UserPhoto;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
@Builder
public class UserProfileDto {
    private Long id;
    @NotBlank(message = "Please fill the Username!")
    private String username;
    private Gender gender;
    private String profilePhoto;
    @Email(message = "Email is not correct!")
    @NotBlank(message = "Please fill the Email!")
    private String email;
    private Set<Role> roles;
    private Set<UserPhoto> userPhotos;
}
