package com.tkachuk.pet.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Declaration of a bean for a ModelMapper(M.M. is using for DTO-Entity converting;
     *
     * @return - new bean;
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Serving static resources from the classpath;
     *
     * @param registry;
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/petProject/**")
                .addResourceLocations("file:uploads");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
}
