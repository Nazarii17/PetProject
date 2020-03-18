package com.tkachuk.pet.repositories;

import com.tkachuk.pet.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepo extends JpaRepository<Photo, Long> {
}
