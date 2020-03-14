package com.tkachuk.pet.mappers;

import com.tkachuk.pet.dtos.UserDto;
import com.tkachuk.pet.entities.User;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper mapper;

    @Autowired
    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public UserDto toDto(@NonNull User user) {
        return mapper.map(user, UserDto.class);
    }}




