package com.tkachuk.pet.mappers;

import com.tkachuk.pet.dto.OrganizationCommonInfoDto;
import com.tkachuk.pet.entities.Organization;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrganizationMapper {

    private final ModelMapper mapper;

    @Autowired
    public OrganizationMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Organization toEntity(@NonNull OrganizationCommonInfoDto organizationCommonInfoDto) {

        return mapper.map(organizationCommonInfoDto, Organization.class);
    }

    public OrganizationCommonInfoDto toCommonInfoDto(@NonNull Organization organization) {
        return mapper.map(organization, OrganizationCommonInfoDto.class);
    }

    public List<OrganizationCommonInfoDto> toCommonInfoDtoList(@NonNull Collection<Organization> organizations) {
        return organizations
                .stream()
                .map(this::toCommonInfoDto)
                .collect(Collectors.toList());
    }



}
