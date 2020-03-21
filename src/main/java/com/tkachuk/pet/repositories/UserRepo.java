package com.tkachuk.pet.repositories;

import com.tkachuk.pet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);

    Collection<User> findByUsernameStartsWith(String wantedName);
}
