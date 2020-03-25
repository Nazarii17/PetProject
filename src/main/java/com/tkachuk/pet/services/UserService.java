package com.tkachuk.pet.services;

import com.tkachuk.pet.dto.*;
import com.tkachuk.pet.entities.Role;
import com.tkachuk.pet.entities.User;
import com.tkachuk.pet.mappers.UserMapper;
import com.tkachuk.pet.repositories.UserRepo;
import com.tkachuk.pet.utils.FileUtil;
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
    @Value("${hostname}")
    private String hostname;
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

    public User findByName(String username) {
        return userRepo.findByUsername(username);
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

        User useFromDb = getOne(id);
        User userFromUi = userMapper.toEntity(userAdditionFormWithPasswordDto);

        String userEmail = useFromDb.getEmail();
        boolean isEmailChanged = (userFromUi.getEmail() != null && !userFromUi.getEmail().equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(userFromUi.getEmail()));
        if (isEmailChanged) {
            if (!StringUtils.isEmpty(userFromUi.getEmail())) {
                useFromDb.setActivationCode(UUID.randomUUID().toString());
                useFromDb.setEmail(userFromUi.getEmail());
            }
        }
        String userPassword = useFromDb.getPassword();
        boolean isPasswordChanged = (userFromUi.getPassword() != null && !userFromUi.getPassword().equals(userPassword)) ||
                (userPassword != null && !userPassword.equals(userFromUi.getPassword()));
        if (isPasswordChanged) {
            if (!StringUtils.isEmpty(userFromUi.getPassword())) {
                useFromDb.setActivationCode(UUID.randomUUID().toString());
                useFromDb.setPassword(passwordEncoder.encode(userFromUi.getPassword()));
                sendMessage(useFromDb);
            }
        }
        useFromDb.setUsername(userFromUi.getUsername());
        useFromDb.setRoles(userFromUi.getRoles());

        if (isEmailChanged) {
            sendMessage(useFromDb);
        }
        save(useFromDb);
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

    /**
     * Adds user to db with changes of roles, and active;
     *
     * @param user - to add to DB;
     */
    public void add(User user) {
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        save(user);
    }

    /**
     * Checks whether a given user exists in DB;
     *
     * @param user - user from UI;
     * @return - true if user exists in DB;
     */
    public boolean isUserExist(User user) {
        User userFromDb = findByName(user.getUsername());
        return userFromDb != null;
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
        sendMessage(user);
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

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome on board. Please, visit next link: http://%s/activate/%s",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
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

    public UserDto findUserDto(User user) {
        return userMapper.toUserDto(userRepo.getOne(user.getId()));
    }

    /**
     * Sets a given file to User with given Id;
     *
     * @param id    = Id of user whose photo should be updated;
     * @param photo - New photo to set for user profile;
     * @throws IOException - file errors;
     */
    public void setProfilePhoto(Long id, MultipartFile photo) throws IOException {
        User user = getOne(id);
        if (FileUtil.isFileValid(photo)) {
            String resultFilename = FileUtil.saveFile(uploadPath, photo);
            user.setProfilePhoto(resultFilename);
        }
        save(user);
    }

    /**
     * Updates an username of user ith given id to username of a given userDto fromUi;
     * Takes a username of founded user by Id and username of given userDto;
     * Validates the name;
     * If name was changed send an email to user todo finish user notification;
     *
     * @param id         - = Id of user whose name should be updated;
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
                sendMessage(userFromDb);
            }
        }
        save(userFromDb);
    }
}
