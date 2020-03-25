package com.tkachuk.pet.services;

import com.tkachuk.pet.utils.Notifications;
import com.tkachuk.pet.dto.UserAdditionFormWithPasswordDto;
import com.tkachuk.pet.dto.UserCommonInfoDto;
import com.tkachuk.pet.dto.UserDto;
import com.tkachuk.pet.dto.UserProfileDto;
import com.tkachuk.pet.entities.Role;
import com.tkachuk.pet.entities.User;
import com.tkachuk.pet.mappers.UserMapper;
import com.tkachuk.pet.repositories.UserRepo;
import com.tkachuk.pet.utils.FileUtil;
import com.tkachuk.pet.utils.UserUtil;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    @Qualifier("userBasePath")
    private String uploadPath;

    @Value("${user.photos.filepath}")
    private String userPhotoFilepath;

    private final UserRepo userRepo;
    private final MailSender mailSender;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, MailSender mailSender, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public User getOne(Long id) {
        return userRepo.getOne(id);
    }

    public void save(User user) {
        userRepo.save(user);
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
            if (userFromUi.getRoles().size()>0) {
                userFromDb.setRoles(userFromUi.getRoles());
                mailSender.sendNotification(userFromDb, Notifications.ROLE_UPDATED_BY_ADMINISTRATION);
            }
        }
        save(userFromDb);
    }

    public void update(User user,
                       Long id) {
        User userToSave = getOne(id);
        userToSave.setUsername(user.getUsername());
        userToSave.setPassword(user.getPassword());
        userToSave.setRoles(user.getRoles());
        save(userToSave);
    }

    public void deleteById(long id) {
        userRepo.deleteById(id);
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
     * If name was changed send an email to user todo finish user notification;
     *
     * @param id         - = ID of user whose name should be updated;
     * @param userFromUi - UserDto which contains an username which should be used;
     */
    public void updateUsername(Long id, UserDto userFromUi) {
        User userFromDb = getOne(id);

        String username = userFromDb.getUsername();
        boolean isUsernameChanged = (userFromUi.getUsername() != null && !userFromUi.getUsername().equals(username)) ||
                (username != null && !username.equals(userFromUi.getUsername()));
        if (isUsernameChanged) {
            if (!StringUtils.isEmpty(userFromUi.getUsername())) {
                userFromDb.setActivationCode(UUID.randomUUID().toString());
                userFromDb.setUsername(userFromUi.getUsername());
                mailSender.sendNotification(userFromDb, Notifications.NAME_UPDATED);
            }
        }
        save(userFromDb);
    }
}
