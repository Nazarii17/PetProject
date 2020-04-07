package com.tkachuk.pet.service;

import com.tkachuk.pet.dto.OrganizationDto;
import com.tkachuk.pet.dto.OrganizationPhotoDto;
import com.tkachuk.pet.dto.OrganizationProfileDto;
import com.tkachuk.pet.entity.Organization;
import com.tkachuk.pet.entity.OrganizationPhoto;
import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.exception.NoSuchEntityException;
import com.tkachuk.pet.repository.OrganizationRepo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrganizationService {

    /**
     * Gets {@link Organization} from Db,
     * or throws {@link NoSuchEntityException}, if an organization with given id not exists;
     *
     * @param id - {@link Organization} id;
     * @return - {@link Organization} from Db;
     */
    Organization findById(Long id);

    /**
     * Checks whether {@link Organization} with given id exists;
     *
     * @param id - {@link Organization} id;
     * @return - true if {@link Organization} exists;
     */
    boolean isOrganizationExists(Long id);

    /**
     * Finds and returns an {@link OrganizationProfileDto} with given id;
     * Using {@link OrganizationRepo} finds an entity from DB;
     * Then converts to {@link OrganizationProfileDto} format and returns it;
     *
     * @param id - {@link Organization#getId()} id;
     * @return - {@link OrganizationProfileDto};
     */
    Optional<OrganizationProfileDto> findOrganizationProfileById(Long id);

    /**
     * Returns a List of {@link OrganizationDto} which is representation of all {@link Organization} from Db, with DTO pattern;
     * First method founds all {@link Organization} from Db using {@link OrganizationRepo#findAll()},
     * then converts to {@link OrganizationDto} and a list of {@link OrganizationDto} ith overwrote pilePath of logo;
     *
     * @return List of {@link Organization};
     */
    List<OrganizationDto> findAll();

    /**
     * Returns a Set of {@link Organization} photos({@link OrganizationPhotoDto}) found for the ID;
     *
     * @param id - {@link Organization} id;
     * @return - Set of {@link OrganizationPhotoDto};
     */
    Set<OrganizationPhotoDto> findAllPhotosByOrgId(Long id);

    /**
     * Creates and saves {@link Organization} using data of given {@link User} and {@link OrganizationDto};
     *
     * @param user            - @AuthenticationPrincipal version of {@link User};
     * @param organizationDto - {@link OrganizationDto} to save;
     * @return - The non-null value Optional<{{@link OrganizationDto}}>
     */
    Optional<OrganizationDto> create(User user, OrganizationDto organizationDto);

    /**
     * Saves {@link Organization} to Db;
     *
     * @param organization - {@link Organization} to save;
     * @return - saved {@link Organization} with Id;
     */
    Organization save(Organization organization);

    /**
     * Updates {@link Organization} from DB;
     *
     * @param id                     - {@link Organization} id;
     * @param user                   - {@link User} from current session;
     * @param organizationProfileDto - {@link OrganizationProfileDto} f;
     * @return - updated Organization;
     */
    Optional<OrganizationProfileDto> update(Long id,
                                            User user,
                                            OrganizationProfileDto organizationProfileDto);

    /**
     * Deletes {@link Organization} from Db by Id;
     *
     * @param id - {@link Organization} id;
     * @return - 'true' if {@link Organization} was deleted;
     */
    boolean deleteById(Long id);

    /**
     * Changes logo of the {@link Organization} with given id.
     * Finds the {@link Organization} by id.
     * Validates a given {@link MultipartFile} then saves the file;
     * Sets the filename to an organization
     *
     * @param id   - {@link Organization} id of the organization logo of which should be changed;
     * @param file - file from ui to change on;
     * @return
     * @throws IOException - IOException
     */
    Optional<OrganizationDto> changeLogo(Long id, MultipartFile file);

    /**
     * Saves the given logo and sets to an organization, then returns an organization with saved logo;
     * Sets a filename of a given logo to a given organization;
     *
     * @param organization - Organization on which should be putted logo;
     * @param file         - Given logo from user to set up to a given organization;
     * @throws IOException - IO error during saving the file;
     */
    Organization saveLogo(Organization organization, MultipartFile file);

    /**
     * Deletes the logo from found by id {@link Organization;
     *
     * @param id - {@link Organization} id of the organization logo of which should be changed;
     * @return - {@link OrganizationDto} wit no logo;
     */
    Optional<OrganizationDto> deleteLogo(Long id);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Deletes {@link OrganizationPhoto} from Db by Id;
     *
     * @param photoId - {@link OrganizationPhoto} id;
     * @return - 'true' if {@link OrganizationPhoto} was deleted;
     */
    boolean deletePhotoById(Long photoId);

    /**
     * Updates {@link OrganizationPhotoDto} with a given id to a given photo;
     *
     * @param id   - {@link OrganizationPhotoDto} id of photo which should be updated:
     * @param file - {@link MultipartFile} photo which should change existed one;
     * @return - saved {@link OrganizationPhotoDto};
     */
    Optional<OrganizationPhotoDto> updatePhoto(Long id, MultipartFile file);

    /**
     * Creates and saves an {@link OrganizationPhoto} to Db;
     *
     * @param newPhoto - {@link MultipartFile} file to be converted to an {@link OrganizationPhoto};
     * @return saved {@link MultipartFile} with id;
     */
    OrganizationPhoto createOrganizationPhoto(MultipartFile newPhoto);

    /**
     * Sets photos to an Organization with a given Id;
     *
     * @param id     - id Of an Organization to work with;
     * @param photos - given by user from UI photos to be sett to an Organization;
     * @return - Updated Organization;
     */
    Set<OrganizationPhotoDto> addNewPhotos(Long id, MultipartFile[] photos);
    // Todo!!!@Transactional
}
