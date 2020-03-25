package com.tkachuk.pet.mappers;

import com.tkachuk.pet.dto.*;
import com.tkachuk.pet.entities.User;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ModelMapper mapper;

    @Autowired
    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public User toEntity(UserAdditionFormWithPasswordDto userAdditionFormWithPasswordDto) {
        return new User(
                userAdditionFormWithPasswordDto.getId(),
                userAdditionFormWithPasswordDto.getUsername(),
                userAdditionFormWithPasswordDto.getPassword(),
                userAdditionFormWithPasswordDto.getEmail(),
                userAdditionFormWithPasswordDto.getRoles()
        );
    }

    public UserDto toUserDto(@NonNull User user) {
        return mapper.map(user, UserDto.class);
    }

    public UserCommonInfoDto toUserCommonInfoDto(User user) {
        return mapper.map(user, UserCommonInfoDto.class);
    }

    public UserAdditionFormWithPasswordDto toUserAdditionFormWithPasswordDto(User user) {
        return mapper.map(user, UserAdditionFormWithPasswordDto.class);
    }

    public UserProfileDto toUserProfileDto(User user) {
        return mapper.map(user, UserProfileDto.class);
    }

    public List<UserCommonInfoDto> toUserCommonInfoDtoList(@NonNull Collection<User> users) {
        return users
                .stream()
                .map(this::toUserCommonInfoDto)
                .collect(Collectors.toList());
    }
}




