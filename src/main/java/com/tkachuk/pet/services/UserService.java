package com.tkachuk.pet.services;

import com.tkachuk.pet.dto.UserAdditionFormWithNoPasswordDto;
import com.tkachuk.pet.dto.UserAdditionFormWithPasswordDto;
import com.tkachuk.pet.dto.UserCommonInfoDto;
import com.tkachuk.pet.entities.Role;
import com.tkachuk.pet.entities.User;
import com.tkachuk.pet.mappers.UserMapper;
import com.tkachuk.pet.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Value("${hostname}")
    private String hostname;

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

    public void update(UserCommonInfoDto userCommonInfoDto,
                       Long id,
                       String password) {

        User userToSave = getOne(id);
        String userEmail = userToSave.getEmail();
        User userFromUi = userMapper.toEntity(userCommonInfoDto);

        boolean isEmailChanged = (userFromUi.getEmail() != null && !userFromUi.getEmail().equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(userFromUi.getEmail()));
        if (isEmailChanged) {
            userToSave.setEmail(userFromUi.getEmail());
            if (!StringUtils.isEmpty(userFromUi.getEmail())) {
                userToSave.setActivationCode(UUID.randomUUID().toString());
            }
        }
        if (!StringUtils.isEmpty(password)) {
            userToSave.setPassword(passwordEncoder.encode(password));
        }

        userToSave.setUsername(userFromUi.getUsername());
        userToSave.setRoles(userFromUi.getRoles());

        if (isEmailChanged) {
            sendMessage(userToSave);
        }
        save(userToSave);
    }

    public void update(UserAdditionFormWithPasswordDto userAdditionFormWithPasswordDto,
                       Long id) {

        User userToSave = getOne(id);
        User userFromUi = userMapper.fromUserAdditionFormWithPasswordDtoToEntity(userAdditionFormWithPasswordDto);

        String userEmail = userToSave.getEmail();
        boolean isEmailChanged = (userFromUi.getEmail() != null && !userFromUi.getEmail().equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(userFromUi.getEmail()));
        if (isEmailChanged) {

            if (!StringUtils.isEmpty(userFromUi.getEmail())) {
                userToSave.setActivationCode(UUID.randomUUID().toString());
                userToSave.setEmail(userFromUi.getEmail());
            }
        }

        String userPassword = userToSave.getPassword();
        boolean isPasswordChanged = (userFromUi.getPassword() != null && !userFromUi.getPassword().equals(userPassword)) ||
                (userPassword != null && !userPassword.equals(userFromUi.getPassword()));
        if (isPasswordChanged){
            if (!StringUtils.isEmpty(userFromUi.getPassword())) {
                userToSave.setActivationCode(UUID.randomUUID().toString());
                userToSave.setPassword(passwordEncoder.encode(userFromUi.getPassword()));
                sendMessage(userToSave);
            }
        }
        userToSave.setUsername(userFromUi.getUsername());
        userToSave.setRoles(userFromUi.getRoles());

        if (isEmailChanged) {
            sendMessage(userToSave);
        }
        save(userToSave);
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
        return userMapper.toDtoCommonInfoList(userRepo.findAll());
    }

    public UserCommonInfoDto findCommonInfoDtoById(Long id) {
        return userMapper.toCommonInfoDto(userRepo.getOne(id));
    }

    public List<UserCommonInfoDto> findAllCommonInfoDtoUsernameStartsWith(String wantedName) {
        return userMapper.toDtoCommonInfoList(userRepo.findByUsernameStartsWith(wantedName));
    }

    public User fromAuthenticationPrincipalToEntity(User APUser){
        return getOne(APUser.getId());
    }

    public UserAdditionFormWithPasswordDto findUserAdditionFormWithPasswordDtoById(Long id) {
        return userMapper.toUserAdditionFormWithPasswordDto(userRepo.getOne(id));
    }

    public UserAdditionFormWithNoPasswordDto findUserAdditionFormWithNoPasswordDtoById(Long id) {
        return userMapper.toUserAdditionFormWithNoPasswordDto(userRepo.getOne(id));
    }
}