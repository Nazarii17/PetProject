package com.tkachuk.pet.service;

import com.tkachuk.pet.entity.UserPhoto;

public interface UserPhotoService {

    UserPhoto save(UserPhoto userPhoto);

    UserPhoto getOne(Long id);

    void deleteById(long id);

}
