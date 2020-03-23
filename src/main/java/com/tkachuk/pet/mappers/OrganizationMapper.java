package com.tkachuk.pet.mappers;

import com.tkachuk.pet.dto.OrganizationCommonInfoDto;
import com.tkachuk.pet.dto.OrganizationDto;
import com.tkachuk.pet.dto.UserDto;
import com.tkachuk.pet.entities.Organization;
import com.tkachuk.pet.entities.OrganizationPhoto;
import com.tkachuk.pet.entities.User;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
public class OrganizationMapper {

    @Value("${organization.photos.filepath}")
    private String organizationPhotoFilepath;

    private final ModelMapper mapper;
    private final UserMapper userMapper;

    @Autowired
    public OrganizationMapper(ModelMapper mapper, UserMapper userMapper) {
        this.mapper = mapper;
        this.userMapper = userMapper;
    }

    public Organization toEntity(@NonNull OrganizationCommonInfoDto organizationCommonInfoDto) {

        return mapper.map(organizationCommonInfoDto, Organization.class);
    }

    public OrganizationCommonInfoDto toCommonInfoDto(@NonNull Organization organization) {
        return mapper.map(organization, OrganizationCommonInfoDto.class);
    }

    public List<OrganizationCommonInfoDto> toCommonInfoDtoList(@NonNull Collection<Organization> organizations) {
        List<OrganizationCommonInfoDto> list = new ArrayList<>();
        for (Organization organization : organizations) {
            overwriteLogoPath(organization);
            OrganizationCommonInfoDto organizationCommonInfoDto = toCommonInfoDto(organization);
            list.add(organizationCommonInfoDto);
        }
        return list;
    }

    /**
     * Overwrites a logo filepath;
     * Adds 'organizationPhotoFilepath' to the beginning of each OrganizationPhoto's name;
     *
     * @param organization - 'organizationPhotoFilepath' + organization.getLogo();
     */
    public void overwriteLogoPath(Organization organization) {
        if (organization.getLogo() != null) {
            String logo = organizationPhotoFilepath + organization.getLogo();
            organization.setLogo(logo);
        }
    }

    /**
     * Converts a given Organization to a DTO;
     *
     * @param organization - organization to convert;
     * @return - OrganizationDto with values of given organization;
     */
    public OrganizationDto toOrganizationDto(Organization organization) {

        overwriteLogoPath(organization);

        UserDto userDto = userMapper.toDto(organization.getAuthor());

        return OrganizationDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .website(organization.getWebsite())
                .author(userDto)
                .address(organization.getAddress())
                .phoneNumber(organization.getPhoneNumber())
                .rating(organization.getRating())
                .description(organization.getDescription())
                .logo(organization.getLogo())//todo fix
                .organizationTypes(organization.getOrganizationTypes())//todo fix
                .organizationPhotos(overwriteNames(organization.getOrganizationPhotos()))
                .build();
    }

    /**
     * Converts given OrganizationDto to an Entity;
     *
     * @param organizationDto - given DTO to convert to entity;
     * @param user            - User from session; Todo: is user from session?
     * @return - New organization with values from given OrganizationDto;
     */
    public Organization fromOrganizationDtoToEntity(OrganizationDto organizationDto, User user) {
        return new Organization(
                organizationDto.getId(),
                organizationDto.getName(),
                organizationDto.getWebsite(),
                user,
                organizationDto.getAddress(),
                organizationDto.getPhoneNumber(),
                organizationDto.getRating(),
                organizationDto.getDescription(),
                organizationDto.getLogo(),
                organizationDto.getOrganizationTypes(),
                organizationDto.getOrganizationPhotos()
        );
    }

    /**
     * Overwrites names of all OrganizationPhotos of a given Set;
     * Adds 'organizationPhotoFilepath' to the beginning of each OrganizationPhoto's name;
     *
     * @param organizationPhotos - given Set of OrganizationPhotos;
     * @return - Set<OrganizationPhoto> with overwrote names;
     */
    public Set<OrganizationPhoto> overwriteNames(Set<OrganizationPhoto> organizationPhotos) {
        organizationPhotos.forEach(
                organizationPhoto
                        ->
                        organizationPhoto.setName(organizationPhotoFilepath + organizationPhoto.getName()));
        return organizationPhotos;
    }

}
