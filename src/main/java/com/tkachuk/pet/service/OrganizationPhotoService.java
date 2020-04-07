package com.tkachuk.pet.service;

import com.tkachuk.pet.entity.OrganizationPhoto;

public interface OrganizationPhotoService {

    OrganizationPhoto save(OrganizationPhoto organizationPhoto);

    boolean deleteById(Long id);

    OrganizationPhoto findById(Long id);

    boolean isOrganizationPhotoExists(Long id);

}
