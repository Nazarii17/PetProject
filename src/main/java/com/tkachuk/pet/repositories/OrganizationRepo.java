package com.tkachuk.pet.repositories;

import com.tkachuk.pet.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepo extends JpaRepository<Organization, Long> {
    Organization findByName(String name);
}
