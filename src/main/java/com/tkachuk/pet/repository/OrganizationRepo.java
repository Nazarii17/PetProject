package com.tkachuk.pet.repository;

import com.tkachuk.pet.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepo extends JpaRepository<Organization, Long> {
    Organization findByName(String name);
}
