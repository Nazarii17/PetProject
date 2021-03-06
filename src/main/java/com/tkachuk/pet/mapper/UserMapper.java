package com.tkachuk.pet.mapper;

import com.tkachuk.pet.dto.UserAdditionFormWithPasswordDto;
import com.tkachuk.pet.dto.UserDto;
import com.tkachuk.pet.dto.UserProfileDto;
import com.tkachuk.pet.entity.User;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final ModelMapper mapper;

    @Autowired
    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public UserDto toUserDto(@NonNull User user) {
        return mapper.map(user, UserDto.class);
    }

    public UserAdditionFormWithPasswordDto toUserAdditionFormWithPasswordDto(User user) {
        return mapper.map(user, UserAdditionFormWithPasswordDto.class);
    }

    public UserProfileDto toUserProfileDto(User user) {
        return mapper.map(user, UserProfileDto.class);
    }

    public <F,T> User toEntity(@NonNull F f) {
        return mapper.map(f, User.class);
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        return users
                .stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }
}
