package com.tkachuk.pet.dto;

import com.tkachuk.pet.entity.OrganizationType;
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
    private String logo;
    private UserDto author;
    private String address;
    private String phoneNumber;
    private double rating;
    @NotNull
    @NotBlank(message = "Please fill the Description!")
    @Length(max = 2048, message = "Description is to long!")
    private String description;
    private Set<OrganizationType> organizationTypes;
}
