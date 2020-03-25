package com.tkachuk.pet.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter @Setter @ToString
public class UserDto {
    private Long id;
    @NotBlank(message = "Please fill the Username!")
    private String username;
}
