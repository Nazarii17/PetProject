package com.tkachuk.pet.mapper;

import com.tkachuk.pet.dto.OrganizationDto;
import com.tkachuk.pet.dto.OrganizationPhotoDto;
import com.tkachuk.pet.dto.OrganizationProfileDto;
import com.tkachuk.pet.dto.UserDto;
import com.tkachuk.pet.entity.Organization;
import com.tkachuk.pet.entity.User;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrganizationMapper {

    private final ModelMapper mapper;
    private final UserMapper userMapper;
    private final OrganizationPhotoMapper photoMapper;

    /**
     * Converts a given {@link Organization} to a DTO;
     *
     * @param organizations - {@link Organization} to convert;
     * @return - OrganizationLightDto with values of given {@link Organization};
     */
    public List<OrganizationDto> toOrganizationDtoList(@NonNull Collection<Organization> organizations) {
        return organizations.stream()
                .map(this::toOrganizationDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a given {@link Organization} to a {@link OrganizationDto};
     *
     * @param organization - {@link Organization} to convert;
     * @return - {@link OrganizationDto} with values of given {@link Organization};
     */
    public OrganizationDto toOrganizationDto(Organization organization) {
        return mapper.map(organization, OrganizationDto.class);
    }

    /**
     * Converts a given {@link Organization} to a {@link OrganizationProfileDto};
     *
     * @param organization - {@link Organization} to convert;
     * @return - {@link OrganizationProfileDto} with values of given {@link Organization};
     */
    public OrganizationProfileDto toOrganizationProfileDto(Organization organization) {
        UserDto userDto = userMapper.toUserDto(organization.getAuthor());
        Set<OrganizationPhotoDto> organizationPhotos = photoMapper.toDtoSet(organization.getOrganizationPhotos());

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
                .organizationPhotos(organizationPhotos)
                .build();
    }

    /**
     * Converts given any DTO of {@link Organization} to an Entity{@link Organization};
     *
     * @param f    - given DTO to convert to entity;
     * @param user - {@link User} from session;
     * @return - New {@link Organization} with values from given OrganizationDto;
     */
    public <F> Organization toEntity(@NonNull F f, User user) {
        Organization organization = mapper.map(f, Organization.class);
        organization.setAuthor(user);
        return organization;
    }

    /**
     * Converts from any DTO of {@link Organization} to Entity{@link Organization};
     *
     * @param <F> - any DTO of {@link Organization}
     * @return - {@link Organization}
     */
    public <F> Organization toEntity(@NonNull F f) {
        return mapper.map(f, Organization.class);
    }
}
