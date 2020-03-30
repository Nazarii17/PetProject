package com.tkachuk.pet.service;

import com.tkachuk.pet.entity.OrganizationPhoto;

public interface OrganizationPhotoService {

    OrganizationPhoto save(OrganizationPhoto organizationPhoto);

    void deleteById(long id);

    OrganizationPhoto getOne(Long id);

}
