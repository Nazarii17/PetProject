package com.tkachuk.pet.mappers;

import com.tkachuk.pet.dtos.OrganizationDto;
import com.tkachuk.pet.entities.Organization;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OrganizationMapper {

    private final ModelMapper mapper;

    @Autowired
    public OrganizationMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Organization toEntity(@NonNull OrganizationDto organizationDto) {
        return Objects.isNull(organizationDto) ? null : mapper.map(organizationDto, Organization.class);
    }

    public OrganizationDto toDto(@NonNull Organization organization) {
        return Objects.isNull(organization) ? null : mapper.map(organization, OrganizationDto.class);
    }

    public List<OrganizationDto> toDtoList(@NonNull Collection<Organization> organizations) {
        return organizations
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
