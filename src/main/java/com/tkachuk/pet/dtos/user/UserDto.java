package com.tkachuk.pet.dtos.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class UserDto {
    private Long id;
    private String username;
}
