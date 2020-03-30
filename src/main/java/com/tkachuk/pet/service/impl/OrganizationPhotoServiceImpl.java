package com.tkachuk.pet.service.impl;

import com.tkachuk.pet.entity.OrganizationPhoto;
import com.tkachuk.pet.repository.OrganizationPhotoRepo;
import com.tkachuk.pet.service.OrganizationPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationPhotoServiceImpl implements OrganizationPhotoService {

    private final OrganizationPhotoRepo organizationPhotoRepo;

    @Autowired
    public OrganizationPhotoServiceImpl(OrganizationPhotoRepo organizationPhotoRepo) {
        this.organizationPhotoRepo = organizationPhotoRepo;
    }

    public OrganizationPhoto save(OrganizationPhoto organizationPhoto) {
        return organizationPhotoRepo.save(organizationPhoto);
    }

    public void deleteById(long id) {
        organizationPhotoRepo.deleteById(id);
    }

    public OrganizationPhoto getOne(Long id) {
        return organizationPhotoRepo.getOne(id);
    }
}
