package com.tkachuk.pet.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;
    @Column
    @NotBlank(message = "Please fill the Username!")
    private String username;
    @Column
    @NotBlank(message = "Please fill the Password!")
    private String password;
    @Column
    private boolean active;
    @Column
    @Email(message = "Email is not correct!")
    @NotBlank(message = "Please fill the Email!")
    private String email;
    @Column(name = "activation_code")
    private String activationCode;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<UserPhoto> userPhotos;

    public User(Long id,
                @NotBlank(message = "Please fill the Username!") String username,
                @Email(message = "Email's not correct!")
                @NotBlank(message = "Please fill the Email!") String email,
                Set<Role> roles
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public User(Long id, @NotBlank(message = "Please fill the Username!") String username, @NotBlank(message = "Please fill the Password!") String password, @Email(message = "Email is not correct!") @NotBlank(message = "Please fill the Email!") String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }
}