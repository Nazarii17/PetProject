package com.tkachuk.pet.service.impl;

import com.tkachuk.pet.constant.ErrorMessage;
import com.tkachuk.pet.entity.OrganizationPhoto;
import com.tkachuk.pet.exception.NoSuchEntityException;
import com.tkachuk.pet.exception.NotSavedException;
import com.tkachuk.pet.repository.OrganizationPhotoRepo;
import com.tkachuk.pet.service.OrganizationPhotoService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizationPhotoServiceImpl implements OrganizationPhotoService {

    private final OrganizationPhotoRepo organizationPhotoRepo;

    /**
     * Gets {@link OrganizationPhoto} from Db,
     * or throws {@link NoSuchEntityException}, if an organization photo with given id not exists;
     *
     * @param id - {@link OrganizationPhoto} id;
     * @return - {@link OrganizationPhoto} from Db;
     */
    @Override
    public OrganizationPhoto findById(Long id) {
        return organizationPhotoRepo.findById(id)
                .orElseThrow(() ->
                        new NoSuchEntityException(ErrorMessage.ORGANIZATION_PHOTO_NOT_FOUND_BY_ID + id));
    }

    /**
     * Checks whether {@link OrganizationPhoto} with given id exists;
     *
     * @param id - {@link OrganizationPhoto} id;
     * @return - true if {@link OrganizationPhoto} exists;
     */
    @Override
    public boolean isOrganizationPhotoExists(Long id) {
        return organizationPhotoRepo.findById(id).isPresent();
    }

    /**
     * Deletes {@link OrganizationPhoto} from Db by Id;
     *
     * @param id - {@link OrganizationPhoto} id;
     * @return - 'true' if {@link OrganizationPhoto} was deleted;
     */
    @Override
    public boolean deleteById(Long id) {
        if (isOrganizationPhotoExists(id)) {
            organizationPhotoRepo.deleteById(id);
            return true;
        } else return false;
    }

    /**
     * Saves {@link OrganizationPhoto} to Db;
     *
     * @param organizationPhoto - {@link OrganizationPhoto} to save;
     * @return - saved {@link OrganizationPhoto} with Id;
     */
    @Override
    public OrganizationPhoto save(OrganizationPhoto organizationPhoto) {
        try {
            organizationPhotoRepo.save(organizationPhoto);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException(ErrorMessage.ORGANIZATION_PHOTO_NOT_SAVED);
        }
        return organizationPhoto;
    }

}
