package com.tkachuk.pet.dtos.organization;

import com.tkachuk.pet.dtos.user.UserDto;
import com.tkachuk.pet.entities.OrganizationPhoto;
import com.tkachuk.pet.entities.OrganizationType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
@Builder
public class OrganizationDto {
    private Long id;
    @NotNull
    @NotBlank(message = "Please fill the name!")
    private String name;
    @NotNull
    @NotBlank(message = "Please fill the website!")
    private String website;
    private UserDto author;
    private String address;
    private String phoneNumber;
    private double rating;
    @NotNull
    @NotBlank(message = "Please fill the Description!")
    @Length(max = 2048, message = "Description is to long!")
    private String description;
    private String logo;
    private Set<OrganizationType> organizationTypes;
    private Set<OrganizationPhoto> organizationPhotos;
}