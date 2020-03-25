package com.tkachuk.pet.service;

import com.tkachuk.pet.dto.OrganizationCommonInfoDto;
import com.tkachuk.pet.dto.OrganizationDto;
import com.tkachuk.pet.entity.Organization;
import com.tkachuk.pet.entity.OrganizationPhoto;
import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.mapper.OrganizationMapper;
import com.tkachuk.pet.repository.OrganizationRepo;
import com.tkachuk.pet.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class OrganizationService {

    @Autowired
    @Qualifier("basePath")
    private String uploadPath;

    private final OrganizationRepo organizationRepo;
    private final OrganizationMapper organizationMapper;
    private final PhotoService photoService;
    private final UserService userService;

    @Autowired
    public OrganizationService(OrganizationRepo organizationRepo,
                               OrganizationMapper organizationMapper, PhotoService photoService, UserService userService) {
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
        return organizationRepo.getOne(id);
    }

    public void delete(long id) {
        organizationRepo.deleteById(id);
    }

    /**
     * Sets given logo to a given organization;
     *
     * @param organization - Organization on which should be putted logo;
     * @param file         - Given logo from user to set up to a given organization;
     * @throws IOException - IO error during saving the file;
     */
    public void setLogo(Organization organization,
                        MultipartFile file) throws IOException {

        if (FileUtil.isFileValid(file)) {
            String resultFilename = FileUtil.saveFile(uploadPath, file);
            organization.setLogo(resultFilename);
        }
    }

    /**
     * Finds and returns a OrganizationDto by given id;
     * Using 'organizationRepo' finds an entity from DB;
     * Then converts to 'OrganizationDto' format nd returns it  returns;
     *
     * @param id - Given id from UI;
     * @return - OrganizationDto;
     */
    public OrganizationDto findDtoById(Long id) {
        return organizationMapper.toOrganizationDto(organizationRepo.getOne(id));
    }

    /**
     * Returns a List of all 'OrganizationCommonInfoDto';
     * First method founds all organizations using 'organizationRepo',
     * then converts to 'OrganizationCommonInfoDto' and returns;
     *
     * @return - List of all 'OrganizationCommonInfoDto';
     */
    public List<OrganizationCommonInfoDto> findAllCommonInfoDto() {
        return organizationMapper.toCommonInfoDtoList(organizationRepo.findAll());
    }

    /**
     * Saves an organization with Valid author, and file;
     *
     * @param user            - user from current session;
     * @param organizationDto - value from UI form, made by user;
     * @param file            - (image) logo of an organization;
     * @throws IOException - error with file process;
     */
    public void save(User user,
                     OrganizationDto organizationDto,
                     MultipartFile file
    ) throws IOException {
        User userFromDb = userService.fromAuthenticationPrincipalToEntity(user);// TODO Is it ok?
        Organization organization = organizationMapper.fromOrganizationDtoToEntity(organizationDto, userFromDb);// TODO Is it ok?

        setLogo(organization, file);
        save(organization);
    }

    /**
     * Updates an organization from DB;
     *
     * @param id              - id of Organization(uses to find current organization in DB);
     * @param user            - user from current session;
     * @param organizationDto - value from UI form, made by user;
     * @param file            - (image) logo of an organization;
     * @throws IOException - error with file process;
     */
    public void update(Long id,
                       User user,
                       OrganizationDto organizationDto,
                       MultipartFile file) throws IOException {

        Organization organization = organizationMapper.fromOrganizationDtoToEntity(organizationDto, user);

        Organization organizationFromDb = getOne(id);
        User userFromDb = userService.fromAuthenticationPrincipalToEntity(user);

        organizationFromDb.setName(organization.getName());
        organizationFromDb.setWebsite(organization.getWebsite());
        organizationFromDb.setAddress(organization.getAddress());
        organizationFromDb.setAuthor(userFromDb);// TODO Is it ok?
        organizationFromDb.setPhoneNumber(organization.getPhoneNumber());
        organizationFromDb.setRating(organization.getRating());
        organizationFromDb.setDescription(organization.getDescription());
        setLogo(organizationFromDb, file);
        organizationFromDb.setOrganizationTypes(organization.getOrganizationTypes());

        save(organizationFromDb);
    }

    //Todo should it be here or PhotoService?

    /**
     * Sets photos to an Organization ith a given Id;
     *
     * @param id     - id Of an Organization to work with;
     * @param photos - given by user from UI photos to be sett to an Organization;
     * @return - Updated Organization;
     * @throws IOException - File error;
     */
    public Organization addNewPhotos(Long id,
                                     MultipartFile[] photos) throws IOException {
        Organization organization = getOne(id);
        Set<OrganizationPhoto> organizationOrganizationPhotos = organization.getOrganizationPhotos();
        for (MultipartFile newPhoto : photos) {
            OrganizationPhoto organizationPhoto = new OrganizationPhoto();
            if (FileUtil.isFileValid(newPhoto)) {
                photoService.updatePhotoName(newPhoto, organizationPhoto);
                organizationOrganizationPhotos.add(photoService.save(organizationPhoto));
            }
        }
        organization.setOrganizationPhotos(organizationOrganizationPhotos);
        return save(organization);
    }



}