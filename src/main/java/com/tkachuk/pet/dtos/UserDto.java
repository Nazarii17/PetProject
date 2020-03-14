package com.tkachuk.pet.dtos;

import com.tkachuk.pet.entities.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter @Setter @ToString
public class UserDto {
    private Long id;
    private String username;
    private Set<Role> roles;
}
