package com.tkachuk.pet.services;

import com.tkachuk.pet.dtos.OrganizationCommonInfoDto;
import com.tkachuk.pet.dtos.OrganizationDto;
import com.tkachuk.pet.dtos.UserDto;
import com.tkachuk.pet.entities.Organization;
import com.tkachuk.pet.entities.OrganizationType;
import com.tkachuk.pet.entities.Photo;
import com.tkachuk.pet.entities.User;
import com.tkachuk.pet.mappers.OrganizationMapper;
import com.tkachuk.pet.mappers.UserMapper;
import com.tkachuk.pet.repositories.OrganizationRepo;
import com.tkachuk.pet.utils.ControllerUtils;
import com.tkachuk.pet.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class OrganizationService {

    @Value("${upload.path}")
    private String uploadPath;

    private final OrganizationRepo organizationRepo;
    private final OrganizationMapper organizationMapper;
    private final UserMapper userMapper;
    private final PhotoService photoService;

    @Autowired
    public OrganizationService(OrganizationRepo organizationRepo,
                               UserMapper userMapper,
                               OrganizationMapper organizationMapper, PhotoService photoService) {
        this.organizationRepo = organizationRepo;
        this.userMapper = userMapper;
        this.organizationMapper = organizationMapper;
        this.photoService = photoService;
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
            String resultFilename = FileUtil.getResultFilename(file, uploadPath);
            FileUtil.saveFile(uploadPath, file, resultFilename);
            organization.setLogo(resultFilename);
        }
    }

    /**
     * Returns to user an 'organizationAdd' page with errors, to give an user a chance fix the problems
     * and fill up fields correctly;
     *
     * @param organizationDto - Representation of current entity;
     * @param bindingResult   - Container with errors from entity/dto;
     * @param model           - Container which migrates from backend to frontend;
     * @return - organizationAdd template
     */
    public String getAdditionPageWithErrors(OrganizationDto organizationDto,
                                            BindingResult bindingResult,
                                            Model model) {
        Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
        model.mergeAttributes(errorsMap);
        model.addAttribute("organizationDto", organizationDto);
        model.addAttribute("types", OrganizationType.values());
        return "organizationAdd";
    }

    /**
     * Returns to user an 'info' page with errors, to give an user a chance fix the problems
     * and fill up fields correctly;
     *
     * @param organizationDto - Representation of current entity;
     * @param bindingResult   - Container with errors from entity/dto;
     * @param model           - Container which migrates from backend to frontend;
     * @return - info template
     */
    public String getInfoPageWithErrors(OrganizationDto organizationDto,
                                        BindingResult bindingResult,
                                        Model model) {
        Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
        model.mergeAttributes(errorsMap);

        organizationDto.setAuthor(findDtoById(organizationDto.getId()).getAuthor());

        model.addAttribute("organizationDto", organizationDto);
        model.addAttribute("types", OrganizationType.values());

        return "info";
    }

    /**
     * Converts a given Organization to a DTO;
     *
     * @param organization - organization to convert;
     * @return - OrganizationDto vith values of given organization;
     */
    private OrganizationDto toDto(Organization organization) {
        UserDto userDto = userMapper.toDto(organization.getAuthor());
        return OrganizationDto.builder()
                .id(organization.getId())
                .name(organization.getName())
                .website(organization.getWebsite())
                .author(userDto)
                .address(organization.getAddress())
                .phoneNumber(organization.getPhoneNumber())
                .rating(organization.getRating())
                .description(organization.getDescription())
                .logo(organization.getLogo())
                .organizationTypes(organization.getOrganizationTypes())
                .photos(organization.getPhotos())
                .build();
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
        return toDto(organizationRepo.getOne(id));
    }

    /**
     * Returns a List of all 'OrganizationCommonInfoDto';
     * First method founds all organizations using 'organizationRepo',
     * then converts to 'OrganizationCommonInfoDto' and returns;
     *
     * @return - List of all 'OrganizationCommonInfoDto';
     */
    public List<OrganizationCommonInfoDto> findAllCommonInfoDto() {
        return organizationMapper.toDtoNList(organizationRepo.findAll());
    }

    /**
     * Converts given OrganizationDto to an Entity;
     *
     * @param organizationDto - given DTO to convert to entity;
     * @param user            - User from session; Todo: is user from session?
     * @return - New organization with values from given OrganizationDto;
     */
    private Organization toEntity(OrganizationDto organizationDto, User user) {
        return new Organization(
                organizationDto.getId(),
                organizationDto.getName(),
                organizationDto.getWebsite(),
                user,
                organizationDto.getAddress(),
                organizationDto.getPhoneNumber(),
                organizationDto.getRating(),
                organizationDto.getDescription(),
                organizationDto.getLogo(),
                organizationDto.getOrganizationTypes(),
                organizationDto.getPhotos()
        );
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
//        System.err.println(Arrays.toString(photos));

        Organization organization = toEntity(organizationDto, user);
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

        Organization organization = toEntity(organizationDto, user);

        Organization organizationToSave = getOne(id);

        organizationToSave.setName(organization.getName());
        organizationToSave.setWebsite(organization.getWebsite());
        organizationToSave.setAddress(organization.getAddress());
        organizationToSave.setAuthor(user);
        organizationToSave.setPhoneNumber(organization.getPhoneNumber());
        organizationToSave.setRating(organization.getRating());
        organizationToSave.setDescription(organization.getDescription());
        setLogo(organizationToSave, file);
        organizationToSave.setOrganizationTypes(organization.getOrganizationTypes());

        save(organizationToSave);
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
        Set<Photo> organizationPhotos = organization.getPhotos();
        for (MultipartFile newPhoto : photos) {
            Photo photo = new Photo();
            if (FileUtil.isFileValid(newPhoto)) {
                photoService.updatePhotoName(newPhoto, photo);
                organizationPhotos.add(photoService.save(photo));
            }
        }
        organization.setPhotos(organizationPhotos);
        return save(organization);
    }
}