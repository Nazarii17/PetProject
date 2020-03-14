package com.tkachuk.pet.services;

import com.tkachuk.pet.entities.Role;
import com.tkachuk.pet.entities.User;
import com.tkachuk.pet.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User findByName(String username) {
        return userRepo.findByUsername(username);
    }

    public User getOne(Long id) {
        return userRepo.getOne(id);
    }

    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public void update(@Valid User user, @PathVariable("id") Long id) {
        User userToSave = getOne(id);
        userToSave.setUsername(user.getUsername());
        userToSave.setPassword(user.getPassword());
        userToSave.setRoles(user.getRoles());
        save(userToSave);
    }

    public void deleteById(long id) {
        userRepo.deleteById(id);
    }

    /**
     * Adds user to db with changes of roles, and active;
     *
     * @param user - to add to DB;
     */
    public void add(User user) {
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        save(user);
    }

    /**
     * Checks whether a given user exists in DB;
     *
     * @param user - user from UI;
     * @return - true if user exists in DB;
     */
    public boolean isUserExist(User user) {
        User userFromDb = findByName(user.getUsername());
        return userFromDb != null;
    }
}