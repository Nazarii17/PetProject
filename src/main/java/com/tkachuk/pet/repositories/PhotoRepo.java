package com.tkachuk.pet.repositories;

import com.tkachuk.pet.entities.OrganizationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepo extends JpaRepository<OrganizationPhoto, Long> {
}
