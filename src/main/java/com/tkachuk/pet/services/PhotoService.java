package com.tkachuk.pet.services;

import com.tkachuk.pet.entities.Photo;
import com.tkachuk.pet.repositories.PhotoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {

    private final PhotoRepo photoRepo;

    @Autowired
    public PhotoService(PhotoRepo photoRepo) {
        this.photoRepo = photoRepo;
    }


    public Photo save(Photo photo) {
        return photoRepo.save(photo);
    }
}
