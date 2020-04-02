package com.tkachuk.pet.config;

import com.tkachuk.pet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//allows to use some parts of the app for example just for Admins
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //    necessary to prevent security from being applied to the resources
        //    such as CSS, images and javascript
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/templates/**", "/css/**", "/js/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .authorizeRequests()
                    .antMatchers("/", "/registration", "/static/**", "/activate/*").permitAll()
                    .antMatchers("/users/**").access("hasAnyAuthority('USER')")
                    .antMatchers("/admin/**").access("hasAnyAuthority('ADMIN')")
                    .anyRequest().authenticated()
                .and()
//                    .csrf().disable()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();

        //TODO How to use?
//        String [] publicUrls = new String [] {
//                "/organizations/**"
//        };
//        http.csrf()
//                .ignoringAntMatchers(publicUrls);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}
