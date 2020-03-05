package com.tkachuk.pet.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class OrganizationDto {

    private Long id;
    private String name;
    private String website;
    private double rating;
    private String description;
    private String logo;
}