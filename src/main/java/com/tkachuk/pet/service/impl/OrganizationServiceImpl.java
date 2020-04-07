package com.tkachuk.pet.service.impl;

import com.tkachuk.pet.constant.ErrorMessage;
import com.tkachuk.pet.dto.OrganizationDto;
import com.tkachuk.pet.dto.OrganizationPhotoDto;
import com.tkachuk.pet.dto.OrganizationProfileDto;
import com.tkachuk.pet.entity.Organization;
import com.tkachuk.pet.entity.OrganizationPhoto;
import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.exception.NoSuchEntityException;
import com.tkachuk.pet.exception.NotSavedException;
import com.tkachuk.pet.mapper.OrganizationMapper;
import com.tkachuk.pet.mapper.OrganizationPhotoMapper;
import com.tkachuk.pet.repository.OrganizationRepo;
import com.tkachuk.pet.service.OrganizationPhotoService;
import com.tkachuk.pet.service.OrganizationService;
import com.tkachuk.pet.service.UserService;
import com.tkachuk.pet.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrganizationServiceImpl implements OrganizationService {

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
    private String filepathPhoto;

    private final OrganizationRepo organizationRepo;
    private final OrganizationMapper organizationMapper;
    private final OrganizationPhotoMapper organizationPhotoMapper;
    private final OrganizationPhotoService photoService;
    private final UserService userService;

    public OrganizationServiceImpl(OrganizationRepo organizationRepo,
                                   OrganizationMapper organizationMapper,
                                   OrganizationPhotoMapper organizationPhotoMapper,
                                   OrganizationPhotoService photoService,
                                   UserService userService) {
        this.organizationRepo = organizationRepo;
        this.organizationMapper = organizationMapper;
        this.organizationPhotoMapper = organizationPhotoMapper;
        this.photoService = photoService;
        this.userService = userService;
    }

    /**
     * Gets {@link Organization} from Db,
     * or throws {@link NoSuchEntityException}, if an organization with given id not exists;
     *
     * @param id - {@link Organization} id;
     * @return - {@link Organization} from Db;
     */
    @Override
    public Organization findById(Long id) {
        return organizationRepo.findById(id)
                .orElseThrow(() ->
                        new NoSuchEntityException(ErrorMessage.ORGANIZATION_NOT_FOUND_BY_ID + id));
    }

    /**
     * Checks whether {@link Organization} with given id exists;
     *
     * @param id - {@link Organization} id;
     * @return - true if {@link Organization} exists;
     */
    @Override
    public boolean isOrganizationExists(Long id) {
        return organizationRepo.findById(id).isPresent();
    }

    /**
     * Finds and returns an {@link OrganizationProfileDto} with given id;
     * Using {@link OrganizationRepo} finds an entity from DB;
     * Then converts to {@link OrganizationProfileDto} format and returns it;
     *
     * @param id - {@link Organization#getId()} id;
     * @return - {@link OrganizationProfileDto};
     */
    @Override
    public Optional<OrganizationProfileDto> findOrganizationProfileById(Long id) {
        Organization organization = findById(id);
        OrganizationProfileDto dto = organizationMapper.toOrganizationProfileDto(organization);
        setLogoFilePath(filepathLogo, dto);
        setPhotoPaths(filepathPhoto, dto);
        return Optional.of(dto);
    }

    /**
     * Sets filePat for not null {@link Organization#getLogo()};
     * Adds {@link OrganizationServiceImpl#filepathLogo}, on the beginning of not null {@link Organization#getLogo()};
     *
     * @param filepathLogo - {@link OrganizationServiceImpl#filepathLogo};
     * @param organization - {@link Organization};//TODO
     * @return - {@link Organization} with overwrote filePath of logo;//TODO
     */
    private <T extends OrganizationDto> T setLogoFilePath(String filepathLogo, T organization) {
        if (organization.getLogo() != null) {
            organization.setLogo(filepathLogo + organization.getLogo());
        }
        return organization;
    }

    /**
     * Sets filePath for each not null {@link OrganizationPhoto#getName()} of {@link OrganizationProfileDto#getOrganizationPhotos()};
     * Adds {@link OrganizationServiceImpl#filepathPhoto}, on the beginning of aech not null {@link OrganizationPhotoDto#getName()};
     *
     * @param filepathPhoto - {@link OrganizationServiceImpl#filepathPhoto};
     * @param organization  - {@link OrganizationProfileDto};
     * @return - {@link OrganizationProfileDto} with overwrote filePath of each not null {@link OrganizationPhotoDto#getName()};
     */
    private OrganizationProfileDto setPhotoPaths(String filepathPhoto, OrganizationProfileDto organization) {

        Set<OrganizationPhotoDto> photos = organization.getOrganizationPhotos();
        setPhotoNames(photos, filepathPhoto);
        organization.setOrganizationPhotos(photos);
        return organization;
    }

    /**
     * Sets filePath for each not null {@link OrganizationPhoto#getName()} of {@link OrganizationPhoto#getName()};
     *
     * @param filepathPhoto - {@link OrganizationPhoto#getName()};
     * @param filepathPhoto - {@link OrganizationServiceImpl#filepathPhoto};
     * @return - {@link Organization#getOrganizationPhotos()} with overwrote filePath,
     * with {@link OrganizationServiceImpl#filepathPhoto} of each not null {@link OrganizationPhoto#getName()};
     */
    private Set<OrganizationPhotoDto> setPhotoNames(Set<OrganizationPhotoDto> organizationPhotos,
                                                    String filepathPhoto) {
        organizationPhotos.stream()
                .filter(organizationPhoto -> organizationPhoto.getName() != null)
                .forEach(organizationPhoto -> organizationPhoto.setName((filepathPhoto + organizationPhoto.getName())));
        return organizationPhotos;
    }

    /**
     * Returns a List of {@link OrganizationDto} which is representation of all {@link Organization} from Db, with DTO pattern;
     * First method founds all {@link Organization} from Db using {@link OrganizationRepo#findAll()},
     * then converts to {@link OrganizationDto} and a list of {@link OrganizationDto} ith overwrote pilePath of logo;
     *
     * @return List of {@link Organization};
     */
    @Override
    public List<OrganizationDto> findAll() {
        List<Organization> all = organizationRepo.findAll();
        List<OrganizationDto> organizations = organizationMapper.toOrganizationDtoList(all);
        return setLogoFilePaths(filepathLogo, organizations);
    }

    /**
     * Sets {@link OrganizationServiceImpl#filepathLogo} to the beginning of all not null {@link OrganizationDto#getLogo()},
     * from given List of {@link OrganizationDto};
     *
     * @param filepathLogo  - {@link OrganizationServiceImpl#filepathLogo};
     * @param organizations - List of {@link OrganizationDto};
     * @return List of {@link OrganizationDto} with overwrote filePaths of each
     */
    private List<OrganizationDto> setLogoFilePaths(String filepathLogo, List<OrganizationDto> organizations) {
        organizations.stream()
                .filter(organization -> organization.getLogo() != null)
                .forEach(organization -> setLogoFilePath(filepathLogo, organization));
        return organizations;
    }

    /**
     * Returns a Set of {@link Organization} photos({@link OrganizationPhotoDto}) found for the ID;
     *
     * @param id - {@link Organization} id;
     * @return - Set of {@link OrganizationPhotoDto};
     */
    @Override
    public Set<OrganizationPhotoDto> findAllPhotosByOrgId(Long id) {
        Set<OrganizationPhoto> all = findById(id).getOrganizationPhotos();
        Set<OrganizationPhotoDto> photos = organizationPhotoMapper.toDtoSet(all);

        return setPhotoNames(photos, filepathPhoto);
    }

    /**
     * Creates and saves {@link OrganizationDto} using data of given {@link User} and {@link OrganizationDto};
     *
     * @param user            - @AuthenticationPrincipal version of {@link User};
     * @param organizationDto - {@link OrganizationDto} to save;
     * @return - The non-null value Optional<{{@link OrganizationDto}}>
     */
    @Override
    public Optional<OrganizationDto> create(User user, OrganizationDto organizationDto) {
        User userFromDb = userService.findById(user.getId());
        Organization organization = organizationMapper.toEntity(organizationDto, userFromDb);
        save(organization);

        return Optional.of(organizationMapper.toOrganizationDto(organization));
    }

    /**
     * Saves {@link Organization} to Db;
     *
     * @param organization - {@link Organization} to save;
     * @return - saved {@link Organization} with Id;
     */
    @Override
    public Organization save(Organization organization) {
        try {
            organizationRepo.save(organization);
        } catch (DataIntegrityViolationException e) {
            throw new NotSavedException(ErrorMessage.ORGANIZATION_NOT_SAVED);
        }
        return organization;
    }

    /**
     * Updates {@link Organization} from DB;
     *
     * @param id                     - {@link Organization} id;
     * @param user                   - {@link User} from current session;
     * @param organizationProfileDto - {@link OrganizationProfileDto} f;
     * @return - updated Organization;
     */
    @Override
    public Optional<OrganizationProfileDto> update(Long id,
                                                   User user,
                                                   OrganizationProfileDto organizationProfileDto) {

        Organization organizationFromDb = findById(id);
        User userFromDb = userService.findById(user.getId());
        Organization organization = organizationMapper.toEntity(organizationProfileDto);

        organizationFromDb.setName(organization.getName());
        organizationFromDb.setWebsite(organization.getWebsite());
        organizationFromDb.setAddress(organization.getAddress());
        organizationFromDb.setAuthor(userFromDb);
        organizationFromDb.setPhoneNumber(organization.getPhoneNumber());
        organizationFromDb.setRating(organization.getRating());
        organizationFromDb.setDescription(organization.getDescription());
        organizationFromDb.setOrganizationTypes(organization.getOrganizationTypes());

        save(organizationFromDb);

        return Optional.of(organizationMapper.toOrganizationProfileDto(organizationFromDb));
    }

    /**
     * Deletes {@link Organization} from Db by Id;
     *
     * @param id - {@link Organization} id;
     * @return - 'true' if {@link Organization} was deleted;
     */
    @Override
    public boolean deleteById(Long id) {
        if (isOrganizationExists(id)) {
            organizationRepo.deleteById(id);
            return true;
        } else return false;
    }

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
    @Override
    public Optional<OrganizationDto> changeLogo(Long id,
                                                MultipartFile file) {
        Organization organization = findById(id);
        saveLogo(organization, file);
        save(organization);

        return Optional.of(organizationMapper.toOrganizationDto(organization));
    }

    /**
     * Saves the given logo and sets to an organization, then returns an organization with saved logo;
     * Sets a filename of a given logo to a given organization;
     *
     * @param organization - Organization on which should be putted logo;
     * @param file         - Given logo from user to set up to a given organization;
     * @throws IOException - IO error during saving the file;
     */
    public Organization saveLogo(Organization organization, MultipartFile file) {
        String fileName = FileUtil.saveImage(uploadLogoPath, file);
        organization.setLogo(fileName);
        return organization;
    }

    /**
     * Deletes the logo from found by id {@link Organization;
     *
     * @param id - {@link Organization} id of the organization logo of which should be changed;
     * @return - {@link OrganizationDto} wit no logo;
     */
    @Override
    public Optional<OrganizationDto> deleteLogo(Long id) {
        Organization organization = findById(id);
        organization.setLogo(null);
        save(organization);

        return Optional.of(organizationMapper.toOrganizationDto(organization));
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Deletes {@link OrganizationPhoto} from Db by Id;
     *
     * @param photoId - {@link OrganizationPhoto} id;
     * @return - 'true' if {@link OrganizationPhoto} was deleted;
     */
    @Override
    public boolean deletePhotoById(Long photoId) {
        return photoService.deleteById(photoId);
    }

    /**
     * Updates {@link OrganizationPhotoDto} with a given id to a given photo;
     *
     * @param id   - {@link OrganizationPhotoDto} id of photo which should be updated:
     * @param file - {@link MultipartFile} photo which should change existed one;
     * @return - saved {@link OrganizationPhotoDto};
     */
    @Override
    public Optional<OrganizationPhotoDto> updatePhoto(Long id, MultipartFile file) {
        OrganizationPhoto organizationPhoto = photoService.findById(id);
        OrganizationPhoto photo = createOrganizationPhoto(file);
        organizationPhoto.setName(photo.getName());
        photoService.save(organizationPhoto);
        return Optional.of(organizationPhotoMapper.toDto(organizationPhoto));
    }

    /**
     * Creates and saves an {@link OrganizationPhoto} to Db;
     *
     * @param newPhoto - {@link MultipartFile} file to be converted to an {@link OrganizationPhoto};
     * @return saved {@link MultipartFile} with id;
     */
    @Override
    public OrganizationPhoto createOrganizationPhoto(MultipartFile newPhoto) {
        OrganizationPhoto organizationPhoto = new OrganizationPhoto();
        String resultFilename = FileUtil.saveImage(uploadPhotosPath, newPhoto);
        organizationPhoto.setName(resultFilename);
        return photoService.save(organizationPhoto);
    }

    /**
     * Sets photos to an Organization with a given Id;
     *
     * @param id     - id Of an Organization to work with;
     * @param photos - given by user from UI photos to be sett to an Organization;
     * @return - Updated Organization;
     */
    @Override
    public Set<OrganizationPhotoDto> addNewPhotos(Long id, MultipartFile[] photos) {
        Organization organization = findById(id);
        Arrays.stream(photos).forEach(FileUtil::validateImage);
        Set<OrganizationPhoto> organizationPhotos = findById(id).getOrganizationPhotos();
        Arrays.stream(photos)
                .map(this::createOrganizationPhoto)
                .forEach(organizationPhotos::add);
        organization.setOrganizationPhotos(organizationPhotos);
        save(organization);
        return organizationPhotoMapper.toDtoSet(organizationPhotos);
    }
    // Todo!!!@Transactional
}
