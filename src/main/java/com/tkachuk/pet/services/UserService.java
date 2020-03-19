package com.tkachuk.pet.services;

import com.tkachuk.pet.entities.Role;
import com.tkachuk.pet.entities.User;
import com.tkachuk.pet.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final MailSender mailSender;

    @Autowired
    public UserService(UserRepo userRepo, MailSender mailSender) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
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

    public void update(User user,
                       Long id) {
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


    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome on a board. Please, visit next link: http://localhost:8082/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        userRepo.save(user);
        return true;
    }
}