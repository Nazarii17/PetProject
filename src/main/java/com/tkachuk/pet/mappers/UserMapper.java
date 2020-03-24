package com.tkachuk.pet.mappers;

import com.tkachuk.pet.dto.UserAdditionFormWithNoPasswordDto;
import com.tkachuk.pet.dto.UserAdditionFormWithPasswordDto;
import com.tkachuk.pet.dto.UserCommonInfoDto;
import com.tkachuk.pet.dto.UserDto;
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

    public UserDto toDto(@NonNull User user) {
        return mapper.map(user, UserDto.class);
    }

    public List<UserCommonInfoDto> toDtoCommonInfoList(@NonNull Collection<User> users) {
        return users
                .stream()
                .map(this::toCommonInfoDto)
                .collect(Collectors.toList());
    }

    public UserCommonInfoDto toCommonInfoDto(User user) {
        return mapper.map(user, UserCommonInfoDto.class);
    }

    public User toEntity(UserCommonInfoDto userCommonInfoDto) {
        return new User(
                userCommonInfoDto.getId(),
                userCommonInfoDto.getUsername(),
                userCommonInfoDto.getEmail(),
                userCommonInfoDto.getRoles()
        );
    }

    public User fromUserAdditionFormWithPasswordDtoToEntity(UserAdditionFormWithPasswordDto userAdditionFormWithPasswordDto) {
        return new User(
                userAdditionFormWithPasswordDto.getId(),
                userAdditionFormWithPasswordDto.getUsername(),
                userAdditionFormWithPasswordDto.getPassword(),
                userAdditionFormWithPasswordDto.getEmail(),
                userAdditionFormWithPasswordDto.getRoles()
        );
    }

    public UserAdditionFormWithPasswordDto toUserAdditionFormWithPasswordDto(User user) {
        return mapper.map(user, UserAdditionFormWithPasswordDto.class);
    }

    public UserAdditionFormWithNoPasswordDto toUserAdditionFormWithNoPasswordDto(User user) {
        return mapper.map(user, UserAdditionFormWithNoPasswordDto.class);
    }
}




