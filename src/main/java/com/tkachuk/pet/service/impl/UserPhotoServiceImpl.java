package com.tkachuk.pet.service.impl;

import com.tkachuk.pet.entity.UserPhoto;
import com.tkachuk.pet.repository.UserPhotoRepo;
import com.tkachuk.pet.service.UserPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPhotoServiceImpl implements UserPhotoService {

    private final UserPhotoRepo userPhotoRepo;

    @Autowired
    public UserPhotoServiceImpl(UserPhotoRepo userPhotoRepo) {
        this.userPhotoRepo = userPhotoRepo;
    }

    public UserPhoto save(UserPhoto userPhoto) {
        return userPhotoRepo.save(userPhoto);
    }

    public UserPhoto getOne(Long id) {
        return userPhotoRepo.getOne(id);
    }

    public void deleteById(long id) {
        userPhotoRepo.deleteById(id);
    }
}
