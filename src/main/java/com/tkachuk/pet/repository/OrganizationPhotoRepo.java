package com.tkachuk.pet.repository;

import com.tkachuk.pet.entity.OrganizationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationPhotoRepo extends JpaRepository<OrganizationPhoto, Long> {
}
