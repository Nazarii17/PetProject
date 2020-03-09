package com.tkachuk.pet.services;

import com.tkachuk.pet.entities.User;
import com.tkachuk.pet.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User findByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public void save(User user) {
        userRepo.save(user);
    }
}
