package com.tkachuk.pet.services;

import com.tkachuk.pet.entities.Organization;
import com.tkachuk.pet.repositories.OrganizationRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    private final OrganizationRepo organizationRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public OrganizationService(OrganizationRepo organizationRepo, ModelMapper modelMapper) {
        this.organizationRepo = organizationRepo;
        this.modelMapper = modelMapper;
    }

    public List<Organization> findAll() {
        return organizationRepo.findAll();
    }


    public Organization findByName(String name){
        return organizationRepo.findByName(name);
    }

    public Organization getOne(Long id){
        return organizationRepo.getOne(id);
    }

    public void organizationSave(Organization organization){
        organizationRepo.save(organization);
    }

    public void delete(long id) {
        organizationRepo.deleteById(id);
    }
}
