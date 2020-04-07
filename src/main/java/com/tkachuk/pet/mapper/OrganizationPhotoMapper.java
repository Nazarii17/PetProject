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

    /**
     * Converts from {@link OrganizationPhoto} to {@link OrganizationPhotoDto};
     *
     * @param organizationPhoto - {@link OrganizationPhoto}
     * @return - {@link OrganizationPhotoDto};
     */
    public OrganizationPhotoDto toDto(@NonNull OrganizationPhoto organizationPhoto) {
        return mapper.map(organizationPhoto, OrganizationPhotoDto.class);
    }

    /**
     * Converts from Set of {@link OrganizationPhoto} to Set of {@link OrganizationPhotoDto};
     *
     * @param photos - Set of {@link OrganizationPhoto}
     * @return - Set of {@link OrganizationPhotoDto};
     */
    public Set<OrganizationPhotoDto> toDtoSet(Set<OrganizationPhoto> photos) {
        return photos
                .stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

}
