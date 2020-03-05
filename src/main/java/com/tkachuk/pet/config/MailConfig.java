package com.tkachuk.pet.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
