package com.tkachuk.pet.service;

import com.tkachuk.pet.entity.*;
import com.tkachuk.pet.util.Notifications;
import com.tkachuk.pet.dto.UserAdditionFormWithPasswordDto;
import com.tkachuk.pet.dto.UserCommonInfoDto;
import com.tkachuk.pet.dto.UserDto;
import com.tkachuk.pet.dto.UserProfileDto;
import com.tkachuk.pet.mapper.UserMapper;
import com.tkachuk.pet.repository.UserRepo;
import com.tkachuk.pet.util.FileUtil;
import com.tkachuk.pet.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    @Qualifier("userBasePath")
    private String uploadPath;

    @Value("${user.photos.filepath}")
    private String userPhotoFilepath;

    @Value("${user.common.photos.filepath}")
    private String userCommonPhotosFilepath;

    private final UserRepo userRepo;
    private final MailSender mailSender;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PhotoService photoService;

    @Autowired
    public UserService(UserRepo userRepo, MailSender mailSender, UserMapper userMapper, PasswordEncoder passwordEncoder, PhotoService photoService) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.photoService = photoService;
    }

    public List<User> findAll() {
        return userRepo.findAll();
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public void update(UserAdditionFormWithPasswordDto userAdditionFormWithPasswordDto,
                       Long id) {
        User userFromDb = getOne(id);
        User userFromUi = userMapper.toEntity(userAdditionFormWithPasswordDto);

        if (UserUtil.isEmailChanged(userFromUi, userFromDb.getEmail())) {
            if (!StringUtils.isEmpty(userFromUi.getEmail())) {
                userFromDb.setActivationCode(UUID.randomUUID().toString());
                userFromDb.setEmail(userFromUi.getEmail());
                mailSender.sendNotification(userFromDb, Notifications.EMAIL_UPDATED_BY_ADMINISTRATION);
            }
        }
        if (!passwordEncoder.matches(userFromUi.getPassword(), userFromDb.getPassword())) {
            if (!StringUtils.isEmpty(userFromUi.getPassword())) {
                userFromDb.setPassword(passwordEncoder.encode(userFromUi.getPassword()));
                mailSender.sendNotification(userFromDb, Notifications.PASSWORD_UPDATED_BY_ADMINISTRATION);
            }
        }
        if (UserUtil.isUsernameChanged(userFromUi, userFromDb.getUsername())) {
            if (!StringUtils.isEmpty(userFromUi.getUsername())) {
                userFromDb.setUsername(userFromUi.getUsername());
                mailSender.sendNotification(userFromDb, Notifications.NAME_UPDATED_BY_ADMINISTRATION);
            }
        }
        if (UserUtil.areRolesChanged(userFromUi, userFromDb.getRoles())) {
            if (userFromUi.getRoles().size() > 0) {
                userFromDb.setRoles(userFromUi.getRoles());
                mailSender.sendNotification(userFromDb, Notifications.ROLE_UPDATED_BY_ADMINISTRATION);
            }
        }
        save(userFromDb);
    }

    public void update(UserProfileDto userProfileDto,
                       Long id) {
        User userFromDb = getOne(id);
        User userFromUi = userMapper.toEntity(userProfileDto);

        if (UserUtil.isEmailChanged(userFromUi, userFromDb.getEmail())) {
            if (!StringUtils.isEmpty(userFromUi.getEmail())) {
                userFromDb.setActivationCode(UUID.randomUUID().toString());
                userFromDb.setEmail(userFromUi.getEmail());
                mailSender.sendNotification(userFromDb, Notifications.EMAIL_UPDATED);
            }
        }
        if (UserUtil.isUsernameChanged(userFromUi, userFromDb.getUsername())) {
            if (!StringUtils.isEmpty(userFromUi.getUsername())) {
                userFromDb.setUsername(userFromUi.getUsername());
                mailSender.sendNotification(userFromDb, Notifications.NAME_UPDATED);
            }
        }
        if (UserUtil.isGenderChanged(userFromUi, userFromDb.getGender())) {
            if (!StringUtils.isEmpty(userFromUi.getGender())) {
                userFromDb.setGender(userFromUi.getGender());
                mailSender.sendNotification(userFromDb, Notifications.GENDER_UPDATED);
            }
        }
        save(userFromDb);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        mailSender.sendActivationMessage(user);
        return true;
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

    public List<UserCommonInfoDto> findAllCommonInfoDto() {
        return userMapper.toUserCommonInfoDtoList(userRepo.findAll());
    }

    public List<UserCommonInfoDto> findAllCommonInfoDtoByUsernameStartsWith(String wantedName) {
        return userMapper.toUserCommonInfoDtoList(userRepo.findByUsernameStartsWith(wantedName));
    }

    //Todo mapper?
    public User fromAuthenticationPrincipalToEntity(User APUser) {
        return getOne(APUser.getId());
    }

    public UserAdditionFormWithPasswordDto getOneUserAdditionFormWithPasswordDtoById(Long id) {
        return userMapper.toUserAdditionFormWithPasswordDto(userRepo.getOne(id));
    }

    public UserProfileDto getOneUserProfileDto(Long id) {
        UserProfileDto userProfileDto = userMapper.toUserProfileDto(userRepo.getOne(id));
        //todo here or in mapper?
        if (userProfileDto.getProfilePhoto() != null) {
            userProfileDto.setProfilePhoto(userPhotoFilepath + userProfileDto.getProfilePhoto());
        }
        return userProfileDto;
    }

    public UserDto getOneUserDto(Long id) {
        return userMapper.toUserDto(userRepo.getOne(id));
    }

    /**
     * Sets a given file to User with given Id;
     *
     * @param id    = Id of user whose photo should be updated;
     * @param photo - New photo to set for user profile;
     * @throws IOException - file errors;
     */
    public void setProfilePhoto(Long id, MultipartFile photo) throws IOException {
        User userFromDb = getOne(id);
        if (FileUtil.isFileValid(photo)) {
            String resultFilename = FileUtil.saveFile(uploadPath, photo);
            userFromDb.setProfilePhoto(resultFilename);
            mailSender.sendNotification(userFromDb, Notifications.PROFILE_PHOTO_UPDATED);
        }
        save(userFromDb);
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
    public void updateUsername(Long id, UserDto userDto) {
        User userFromDb = getOne(id);
        User userFromUi = userMapper.toEntity(userDto);
        if (UserUtil.isUsernameChanged(userFromUi, userFromDb.getUsername())) {
            if (!StringUtils.isEmpty(userFromUi.getUsername())) {
                userFromDb.setActivationCode(UUID.randomUUID().toString());
                userFromDb.setUsername(userFromUi.getUsername());
                mailSender.sendNotification(userFromDb, Notifications.NAME_UPDATED);
            }
        }
        save(userFromDb);
    }

    public void addNewPhotos(Long id, MultipartFile[] photos) throws IOException {
        User userFromDb = getOne(id);
        Set<UserPhoto> userPhotos = userFromDb.getUserPhotos();
        for (MultipartFile newPhoto : photos) {
            UserPhoto userPhoto = new UserPhoto();
            if (FileUtil.isFileValid(newPhoto)) {
                photoService.updatePhotoName(newPhoto, userPhoto);
                userPhotos.add(photoService.save(userPhoto));
            }
        }
        userFromDb.setUserPhotos(userPhotos);
        save(userFromDb);
    }

    //Todo should it be here or photo service?
    public Set<UserPhoto> getPhotosOfUser(User user) {
        Set<UserPhoto> userPhotos = getOne(user.getId()).getUserPhotos();
        userPhotos.stream().filter(Objects::nonNull).forEach(userPhoto
                ->
                userPhoto.setName(userCommonPhotosFilepath + userPhoto.getName()));
        return userPhotos;
    }
}
