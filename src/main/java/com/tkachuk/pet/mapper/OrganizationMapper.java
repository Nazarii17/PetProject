package com.tkachuk.pet.mapper;

import com.tkachuk.pet.dto.OrganizationDto;
import com.tkachuk.pet.dto.OrganizationProfileDto;
import com.tkachuk.pet.dto.UserDto;
import com.tkachuk.pet.entity.Organization;
import com.tkachuk.pet.entity.User;
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
    private final UserMapper userMapper;

    @Autowired
    public OrganizationMapper(ModelMapper mapper, UserMapper userMapper) {
        this.mapper = mapper;
        this.userMapper = userMapper;
    }

    /**
     * Converts a given Organization to a DTO;
     *
     * @param organizations - organization to convert;
     * @return - OrganizationLightDto with values of given organization;
     */
    public List<OrganizationDto> toOrganizationDtoList(@NonNull Collection<Organization> organizations) {
        return organizations.stream()
                .map(this::toOrganizationDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a given Organization to a DTO;
     *
     * @param organization - organization to convert;
     * @return - OrganizationDto with values of given organization;
     */
    public OrganizationDto toOrganizationDto(Organization organization) {
        return mapper.map(organization, OrganizationDto.class);
    }

    /**
     * Converts a given Organization to a DTO;
     *
     * @param organization - organization to convert;
     * @return - OrganizationProfileDto with values of given organization;
     */
    public OrganizationProfileDto toOrganizationProfileDto(Organization organization) {
        UserDto userDto = userMapper.toUserDto(organization.getAuthor());
        return OrganizationProfileDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .website(organization.getWebsite())
                .author(userDto)
                .address(organization.getAddress())
                .phoneNumber(organization.getPhoneNumber())
                .rating(organization.getRating())
                .description(organization.getDescription())
                .logo(organization.getLogo())
                .organizationTypes(organization.getOrganizationTypes())
                .organizationPhotos(organization.getOrganizationPhotos())
                .build();
    }

    /**
     * Converts given OrganizationDto to an Entity;
     *
     * @param f    - given DTO to convert to entity;
     * @param user - User from session; Todo: is user from session?
     * @return - New organization with values from given OrganizationDto;
     */
    public <F> Organization toEntity(@NonNull F f, User user) {
        Organization organization = mapper.map(f, Organization.class);
        organization.setAuthor(user);
        return organization;
    }

    public <F> Organization toEntity(@NonNull F f) {
        return mapper.map(f, Organization.class);
    }
}
