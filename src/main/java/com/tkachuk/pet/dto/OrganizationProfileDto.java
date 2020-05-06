package com.tkachuk.pet.dto;

import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class OrganizationProfileDto  extends OrganizationDto{
    private Set<OrganizationPhotoDto> organizationPhotos;
}