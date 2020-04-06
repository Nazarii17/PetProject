package com.tkachuk.pet.mapper;

import com.tkachuk.pet.dto.OrganizationPhotoDto;
import com.tkachuk.pet.entity.OrganizationPhoto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrganizationPhotoMapper {

    private final ModelMapper mapper;

    public OrganizationPhotoDto toDto(@NonNull OrganizationPhoto organizationPhoto) {
        return mapper.map(organizationPhoto, OrganizationPhotoDto.class);
    }

    public Set<OrganizationPhotoDto> toDtoList(Set<OrganizationPhoto> photos) {
        return photos
                .stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

}
