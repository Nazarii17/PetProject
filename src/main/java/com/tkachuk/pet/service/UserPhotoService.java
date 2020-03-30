package com.tkachuk.pet.service;

import com.tkachuk.pet.entity.UserPhoto;

public interface UserPhotoService {

    public UserPhoto save(UserPhoto userPhoto);

    public UserPhoto getOne(Long id);

    public void deleteById(long id);

}
