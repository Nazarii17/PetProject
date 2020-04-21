package com.tkachuk.pet.service;

import com.tkachuk.pet.constant.ErrorMessage;
import com.tkachuk.pet.constant.Notification;
import com.tkachuk.pet.dto.UserAdditionFormWithPasswordDto;
import com.tkachuk.pet.dto.UserDto;
import com.tkachuk.pet.dto.UserProfileDto;
import com.tkachuk.pet.entity.Role;
import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.entity.UserPhoto;
import com.tkachuk.pet.exception.NoSuchEntityException;
import com.tkachuk.pet.mapper.UserMapper;
import com.tkachuk.pet.repository.UserRepo;
import com.tkachuk.pet.util.FileUtil;
import com.tkachuk.pet.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    @Qualifier("uploadUserProfilePhotoFilepath")
    private String uploadProfilePhotoPath;

    @Autowired
    @Qualifier("uploadUserPhotosFilepath")
    private String uploadPhotosPath;

    @Autowired
    @Qualifier("filepathUserPhotos")
    private String filepathPhotos;

    @Autowired
    @Qualifier("filepathUserProfilePhoto")
    private String filepathProfilePhoto;

    private final UserRepo userRepo;
    private final MailSender mailSender;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserPhotoService photoService;
    private final UserNotificationsBuilder userNotificationsBuilder;

    @Autowired
    public UserService(UserRepo userRepo, MailSender mailSender, UserMapper userMapper, PasswordEncoder passwordEncoder, UserPhotoService photoService, UserNotificationsBuilder userNotificationsBuilder) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.photoService = photoService;
        this.userNotificationsBuilder = userNotificationsBuilder;
    }

    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new NoSuchEntityException(ErrorMessage.USER_NOT_FOUND_BY_ID + id));
    }

    public User getOne(Long id) {
        return userRepo.getOne(id);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public void deleteById(long id) {
        userRepo.deleteById(id);
    }

    public UserProfileDto findDtoById(Long id) {
        UserProfileDto userProfileDto = userMapper.toUserProfileDto(userRepo.getOne(id));
        return overwriteProfilePhotoPath(filepathProfilePhoto, userProfileDto);
    }

    /**
     * Overwrites a path of a given UserProfileDto;
     * Returns a overwrote UserProfileDto by a pattern userProfilePhotoFilepath  + name of a photo;
     *
     * @param userProfilePhotoFilepath - Bean made in FileStorageConfig;
     * @param userProfileDto           - photo which should be changed;
     * @return - UserProfileDto with new filepath;
     */
    //TODO Should it be here?
    public UserProfileDto overwriteProfilePhotoPath(String userProfilePhotoFilepath, UserProfileDto userProfileDto) {
        if (UserUtil.isProfilePhotoEmpty(userProfileDto)) {
            userProfileDto.setProfilePhoto(userProfilePhotoFilepath + userProfileDto.getProfilePhoto());
        }
        return userProfileDto;
    }

    /**
     * Updates an username of user ith given id to username of a given userDto fromUi;
     * Takes a username of founded user by Id and username of given userDto;
     * Validates the name;
     * If name was changed send an email to user with notification about changed name;
     *
     * @param id      - = ID of user whose name should be updated;
     * @param userDto - UserDto which contains an username which should be used;
     */
    public User updateUsername(Long id, UserDto userDto) {
        User userFromDb = getOne(id);
        User userFromUi = userMapper.toEntity(userDto);
        /// TODO: 31.03.2020 isUsernameValid ok?
        if (UserUtil.isUsernameValid(userFromDb, userFromUi)) {
            userFromDb.setUsername(userFromUi.getUsername());
            mailSender.sendNotification(userFromDb, Notification.NAME_UPDATED);
        }
        return save(userFromDb);
    }

    /**
     * Changes Profile Photo of the user with given id.
     * Finds the user by id.
     * Validates a given file then saves the file;
     * Sets the filename to an user
     *
     * @param id   = Id of user whose photo should be updated;
     * @param file - New photo to set for user profile;
     */
    public User changeProfilePhoto(Long id, MultipartFile file) {
        User user = getOne(id);
        saveProfilePhoto(user, file);
        return save(user);
    }

    /**
     * Saves the given Profile Photo and sets to an user, then returns an user with saved Profile Photo;
     * Sets a filename of a given Profile Photo to a given user;
     *
     * @param user - User on which should be putted Profile Photo;
     * @param file - Given Profile Photo from user to set up to a given User;
     */
    private User saveProfilePhoto(User user, MultipartFile file) {
        String fileName = FileUtil.saveImage(uploadProfilePhotoPath, file);
        user.setProfilePhoto(fileName);
        return user;
    }

    public User updateByAdmin(UserProfileDto userProfileDto, Long id) {
        User userFromDb = getOne(id);
        User userFromUi = userMapper.toEntity(userProfileDto);
        List<Notification> notifications = new ArrayList<>();

        updateFields(userFromDb, userFromUi, notifications);
        mailSender.sendNotification(userFromDb, notifications);
        return save(userFromDb);
    }

    public User updateFields(User userFromDb, User userFromUi, List<Notification> notifications) {
        if (UserUtil.isEmailChanged(userFromUi, userFromDb.getEmail()) && !StringUtils.isEmpty(userFromUi.getEmail())) {
            userFromDb.setActivationCode(UUID.randomUUID().toString());
            userFromDb.setEmail(userFromUi.getEmail());
            notifications.add(Notification.EMAIL_UPDATED);
        }
        if (UserUtil.isUsernameChanged(userFromUi, userFromDb.getUsername()) && !StringUtils.isEmpty(userFromUi.getUsername())) {
            userFromDb.setUsername(userFromUi.getUsername());
            notifications.add(Notification.NAME_UPDATED);
        }
        if (UserUtil.isGenderChanged(userFromUi, userFromDb.getGender()) && !StringUtils.isEmpty(userFromUi.getGender())) {
            userFromDb.setGender(userFromUi.getGender());
            notifications.add(Notification.GENDER_UPDATED);
        }
        return userFromDb;
    }

    public Set<UserPhoto> findAllPhotosByUserId(Long id) {
        Set<UserPhoto> userPhotos = getOne(id).getUserPhotos();
        return overwritePhotoPaths(userPhotos, filepathPhotos);
    }

    public Set<UserPhoto> overwritePhotoPaths(Set<UserPhoto> userPhotos, String filepathPhotos) {
        userPhotos.stream()
                .filter(organizationPhoto ->
                        organizationPhoto.getName() != null)
                .forEach(organizationPhoto ->
                        organizationPhoto.setName((filepathPhotos + organizationPhoto.getName())));
        return userPhotos;
    }

    private UserPhoto createUserPhoto(MultipartFile file) {
        UserPhoto userPhoto = new UserPhoto();
        String resultFilename = FileUtil.saveImage(uploadPhotosPath, file);
        userPhoto.setName(resultFilename);
        return photoService.save(userPhoto);
    }

    public User addNewPhotos(Long id, MultipartFile[] photos) {
        User user = getOne(id);
        Arrays.stream(photos).forEach(FileUtil::isImage);
        Set<UserPhoto> userPhotos = userRepo.getOne(id).getUserPhotos();
        Arrays.stream(photos)
                .map(this::createUserPhoto)
                .forEach(userPhotos::add);
        user.setUserPhotos(userPhotos);
        return save(user);
    }

    public UserPhoto updateUserPhoto(Long id, MultipartFile file) {
        UserPhoto userPhoto = photoService.getOne(id);
        UserPhoto photo = createUserPhoto(file);
        userPhoto.setName(photo.getName());
        return photoService.save(userPhoto);
    }

    public void deleteUserPhoto(long id) {
        photoService.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user) {
        if (isUserExist(user.getUsername())) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getUsername());
        userRepo.save(user);
        mailSender.sendActivationMessage(user);
        return true;
    }

    private boolean isUserExist(String username) {
        return userRepo.findByUsername(username) != null;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        userRepo.save(user);
        return true;
    }

    public User fromAuthenticationPrincipalToEntity(User APUser) {
        return findById(APUser.getId());
    }

    public List<UserDto> findAll() {
        return userMapper.toUserDtoList(userRepo.findAll());
    }

    public List<UserDto> findAllByUsernameStartWith(String wantedName) {
        return userMapper.toUserDtoList(userRepo.findByUsernameStartsWith(wantedName));
    }

    public UserAdditionFormWithPasswordDto getOneUserAdditionFormWithPasswordDtoById(Long id) {
        return userMapper.toUserAdditionFormWithPasswordDto(userRepo.getOne(id));
    }

    public void sendToAll(String message, String subject) {
        for (User user : userRepo.findAll()) {
            mailSender.send(user.getEmail(), subject, message);
        }
    }

    public User updateByAdmin(UserAdditionFormWithPasswordDto userAdditionFormWithPasswordDto,
                              Long id) {
        User userFromDb = getOne(id);
        User userFromUi = userMapper.toEntity(userAdditionFormWithPasswordDto);
        List<Notification> notifications = userNotificationsBuilder.buildChangesNotification(userFromDb, userFromUi);

        if (!notifications.isEmpty()) {
            mailSender.sendNotification(userFromDb, notifications);
        }

        return save(userFromDb);
    }
}
