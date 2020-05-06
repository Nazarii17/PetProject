package com.tkachuk.pet.repository;

import com.tkachuk.pet.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepo extends JpaRepository<Organization, Long> {
    Organization findByName(String name);

    Page<Organization> findAllByNameStartsWith(String wantedName, Pageable pageable);

//    @Query(value = "select * from organization where name = :name", nativeQuery = true)
}
