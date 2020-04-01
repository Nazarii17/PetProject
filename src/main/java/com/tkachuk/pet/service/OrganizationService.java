package com.tkachuk.pet.service;

import com.tkachuk.pet.dto.OrganizationDto;
import com.tkachuk.pet.dto.OrganizationProfileDto;
import com.tkachuk.pet.entity.Organization;
import com.tkachuk.pet.entity.OrganizationPhoto;
import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.exception.NoSuchOrganizationException;
import com.tkachuk.pet.mapper.OrganizationMapper;
import com.tkachuk.pet.repository.OrganizationRepo;
import com.tkachuk.pet.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class OrganizationService {

    @Autowired
    @Qualifier("uploadOrganizationLogoFilepath")
    private String uploadLogoPath;

    @Autowired
    @Qualifier("uploadOrganizationPhotosFilepath")
    private String uploadPhotosPath;

    @Autowired
    @Qualifier("filepathOrganizationLogo")
    private String filepathLogo;

    @Autowired
    @Qualifier("filepathOrganizationPhotos")
    private String filepathPhotos;

    private final OrganizationRepo organizationRepo;
    private final OrganizationMapper organizationMapper;
    private final OrganizationPhotoService photoService;
    private final UserService userService;

    @Autowired
    public OrganizationService(OrganizationRepo organizationRepo,
                               OrganizationMapper organizationMapper,
                               OrganizationPhotoService photoService,
                               UserService userService) {
        this.organizationRepo = organizationRepo;
        this.organizationMapper = organizationMapper;
        this.photoService = photoService;
        this.userService = userService;
    }

    // Todo!!!
    @Transactional
    public void update(Organization organization) {
        //....
        organizationRepo.save(organization);
    }

    public Organization save(Organization organization) {
        return organizationRepo.save(organization);
    }

    public Organization getOne(Long id) {
        return organizationRepo.findById(id)
                .orElseThrow(NoSuchOrganizationException::new);
    }

    public void delete(long id) {
        organizationRepo.deleteById(id);
    }

    /**
     * Changes logo of the organization with given id.
     * Finds the organization by id.
     * Validates a given file then saves the file;
     * Sets the filename to an organization
     *
     * @param id   - id of the organization photo of which should be changed;
     * @param file - file from ui to change on;
     * @throws IOException - IOException
     */
    public void changeLogo(Long id,
                           MultipartFile file) {
        Organization organization = getOne(id);
        saveLogo(organization, file);
        save(organization);
    }

    /**
     * Saves the given logo and sets to an organization, then returns an organization with saved logo;
     * Sets a filename of a given logo to a given organization;
     *
     * @param organization - Organization on which should be putted logo;
     * @param file         - Given logo from user to set up to a given organization;
     * @throws IOException - IO error during saving the file;
     */
    private Organization saveLogo(Organization organization, MultipartFile file) {
        String fileName = FileUtil.savePhoto(uploadLogoPath, file);
        organization.setLogo(fileName);
        return organization;
    }

    /**
     * Finds and returns a OrganizationDto by given id;
     * Using 'organizationRepo' finds an entity from DB;
     * Then converts to 'OrganizationDto' format nd returns it  returns;
     *
     * @param id - Given id from UI;
     * @return - OrganizationDto;
     */
    public OrganizationProfileDto findById(Long id) {
        OrganizationProfileDto organization = organizationMapper.toOrganizationProfileDto(
                organizationRepo.findById(id)
                        .orElseThrow(NoSuchOrganizationException::new));
        //Todo here?
        return overwriteFilePaths(organization);
    }

    public OrganizationProfileDto overwriteFilePaths(OrganizationProfileDto organizationProfileDto) {
        organizationProfileDto = overwriteLogoPath(filepathLogo, organizationProfileDto);

        Set<OrganizationPhoto> organizationPhotos = organizationProfileDto.getOrganizationPhotos();
        organizationProfileDto.setOrganizationPhotos(overwritePhotoPaths(organizationPhotos, filepathPhotos));

        return organizationProfileDto;
    }

    public OrganizationProfileDto overwriteLogoPath(String organizationLogoFilepath,
                                                    OrganizationProfileDto organizationProfileDto) {
        if (organizationProfileDto.getLogo() != null) {
            organizationProfileDto.setLogo(organizationLogoFilepath + organizationProfileDto.getLogo());
        }
        return organizationProfileDto;
    }

    public Set<OrganizationPhoto> overwritePhotoPaths(Set<OrganizationPhoto> organizationPhotos,
                                                      String organizationPhotosFilepath) {
        organizationPhotos.stream()
                .filter(organizationPhoto ->
                        organizationPhoto.getName() != null)
                .forEach(organizationPhoto ->
                        organizationPhoto.setName((organizationPhotosFilepath + organizationPhoto.getName())));
        return organizationPhotos;
    }

    public Set<OrganizationPhoto> findAllPhotosByOrganizationId(Long id) {
        Set<OrganizationPhoto> organizationPhotos = getOne(id).getOrganizationPhotos();
        overwritePhotoPaths(organizationPhotos, filepathPhotos);
        return organizationPhotos;
    }

    public List<OrganizationDto> overwriteLogoPaths(String organizationLogoFilepath,
                                                    List<OrganizationDto> organizations) {
        organizations.stream()
                .filter(organizationDto ->
                        organizationDto.getLogo() != null)
                .forEach(organizationDto ->
                        organizationDto.setLogo(organizationLogoFilepath + organizationDto.getLogo()));
        return organizations;
    }

    /**
     * Returns a List of all 'OrganizationCommonInfoDto';
     * First method founds all organizations using 'organizationRepo',
     * then converts to 'OrganizationCommonInfoDto' and returns;
     *
     * @return - List of all 'OrganizationCommonInfoDto';
     */
    public List<OrganizationDto> findAll() {
        List<OrganizationDto> organizations = organizationMapper.toOrganizationDtoList(organizationRepo.findAll());

        return overwriteLogoPaths(filepathLogo, organizations);
    }

    /**
     * Saves an organization with Valid author, and file;
     *
     * @param user            - user from current session;
     * @param organizationDto - value from UI form, made by user;
     * @return - saved Organization;
     */
    public Organization save(User user, OrganizationDto organizationDto) {
        User userFromDb = userService.getOne(user.getId());
        Organization organization = organizationMapper.toEntity(organizationDto, userFromDb);
        return save(organization);
    }

    /**
     * Updates an organization from DB;
     *
     * @param id                     - id of Organization(uses to find current organization in DB);
     * @param user                   - user from current session;
     * @param organizationProfileDto - value from UI form, made by user;
     * @return - updated Organization;
     */
    public Organization update(Long id,
                               User user,
                               OrganizationProfileDto organizationProfileDto) {

        Organization organization = organizationMapper.toEntity(organizationProfileDto, user);

        Organization organizationFromDb = getOne(id);
        User userFromDb = userService.fromAuthenticationPrincipalToEntity(user);

        organizationFromDb.setName(organization.getName());
        organizationFromDb.setWebsite(organization.getWebsite());
        organizationFromDb.setAddress(organization.getAddress());
        organizationFromDb.setAuthor(userFromDb);
        organizationFromDb.setPhoneNumber(organization.getPhoneNumber());
        organizationFromDb.setRating(organization.getRating());
        organizationFromDb.setDescription(organization.getDescription());
        organizationFromDb.setOrganizationTypes(organization.getOrganizationTypes());

        return save(organizationFromDb);
    }

    /**
     * Sets photos to an Organization with a given Id;
     *
     * @param id     - id Of an Organization to work with;
     * @param photos - given by user from UI photos to be sett to an Organization;
     * @return - Updated Organization;
     */
    public Organization addNewPhotos(Long id,
                                     MultipartFile[] photos) {
        Organization organization = getOne(id);
        Arrays.stream(photos).forEach(FileUtil::validateFile);
        Set<OrganizationPhoto> organizationPhotos = organizationRepo.getOne(id).getOrganizationPhotos();
        Arrays.stream(photos)
                .map(this::createOrganizationPhoto)
                .forEach(organizationPhotos::add);
        organization.setOrganizationPhotos(organizationPhotos);
        return save(organization);
    }

    /**
     * Creates and saves an OrganizationPhoto;
     *
     * @param newPhoto - file to be converted to an OrganizationPhoto;
     * @return saved OrganizationPhoto;
     */
    public OrganizationPhoto createOrganizationPhoto(MultipartFile newPhoto) {
        OrganizationPhoto organizationPhoto = new OrganizationPhoto();
        String resultFilename = FileUtil.savePhoto(uploadPhotosPath, newPhoto);
        organizationPhoto.setName(resultFilename);
        return photoService.save(organizationPhoto);
    }

    /**
     * Updates photo with a given id to a given photo;
     *
     * @param id   - Id of photo which should be updated:
     * @param file - photo which should change existed one;
     * @return - saved OrganizationPhoto;
     */
    public OrganizationPhoto updatePhoto(Long id,
                                         MultipartFile file) {
        OrganizationPhoto organizationPhoto = photoService.getOne(id);
        OrganizationPhoto photo = createOrganizationPhoto(file);
        organizationPhoto.setName(photo.getName());
        return photoService.save(organizationPhoto);
    }

    /**
     * Deletes an Organization photo by Id;
     *
     * @param id - of photo which should be deleted;
     */
    public void deletePhoto(Long id) {
        photoService.deleteById(id);
    }
}
