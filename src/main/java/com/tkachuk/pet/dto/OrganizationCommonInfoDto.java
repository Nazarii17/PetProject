package com.tkachuk.pet.dto;

import lombok.*;

@Getter @Setter @ToString
public class OrganizationCommonInfoDto {
    private Long id;
    private String name;
    private String website;
    private double rating;
    private String description;
    private String logo;
}