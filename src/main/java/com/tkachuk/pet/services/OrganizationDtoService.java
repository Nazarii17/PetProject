package com.tkachuk.pet.services;


import com.tkachuk.pet.dtos.OrganizationDto;
import com.tkachuk.pet.mappers.OrganizationMapper;
import com.tkachuk.pet.repositories.OrganizationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationDtoService {

    private final OrganizationRepo organizationRepo;
    private final OrganizationMapper mapper;

    @Autowired
    public OrganizationDtoService(OrganizationRepo organizationRepo, OrganizationMapper mapper) {
        this.organizationRepo = organizationRepo;
        this.mapper = mapper;
    }

    public OrganizationDto getOne(Long id) {
        return mapper.toDto(organizationRepo.getOne(id));
    }

    public List<OrganizationDto> findAll() {
        return mapper.toDtoList(organizationRepo.findAll());
    }
}
