package com.tkachuk.pet.repository;

import com.tkachuk.pet.entity.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPhotoRepo extends JpaRepository<UserPhoto, Long> {
}
