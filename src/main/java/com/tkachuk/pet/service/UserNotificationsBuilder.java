package com.tkachuk.pet.service;

import com.tkachuk.pet.constants.Notification;
import com.tkachuk.pet.entity.User;
import com.tkachuk.pet.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserNotificationsBuilder {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserNotificationsBuilder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private Optional<Notification> checkEmailChanged(User userFromDb, User userFromUi) {
        if (UserUtil.isEmailValid(userFromDb, userFromUi)) {
            userFromDb.setActivationCode(UUID.randomUUID().toString());
            userFromDb.setEmail(userFromUi.getEmail());
            return Optional.of(Notification.EMAIL_UPDATED_BY_ADMINISTRATION);
        }
        return Optional.empty();
    }

    private Optional<Notification> checkUsernameChanged(User userFromDb, User userFromUi) {
        if (UserUtil.isUsernameChanged(userFromUi, userFromDb.getUsername()) && !StringUtils.isEmpty(userFromUi.getUsername())) {
            userFromDb.setUsername(userFromUi.getUsername());
            return Optional.of(Notification.NAME_UPDATED_BY_ADMINISTRATION);
        }
        return Optional.empty();
    }

    private Optional<Notification> checkPasswordChanged(User userFromDb, User userFromUi) {
        if (UserUtil.isPasswordValid(userFromDb, userFromUi, passwordEncoder)) {
            userFromDb.setPassword(passwordEncoder.encode(userFromUi.getPassword()));
            return Optional.of(Notification.PASSWORD_UPDATED_BY_ADMINISTRATION);
        }
        return Optional.empty();
    }

    private Optional<Notification> checkRolesChanged(User userFromDb, User userFromUi) {
        if (UserUtil.areRolesValid(userFromDb, userFromUi)) {
            userFromDb.setRoles(userFromUi.getRoles());
            return Optional.of(Notification.ROLE_UPDATED_BY_ADMINISTRATION);
        }
        return Optional.empty();
    }

    public List<Notification> buildChangesNotification(User userFromDb, User userFromU) {
        List<Notification> notifications = new ArrayList<>();

        Optional<Notification> emailChangedNotification = checkEmailChanged(userFromDb, userFromU);
        Optional<Notification> usernameChangedNotification = checkUsernameChanged(userFromDb, userFromU);
        Optional<Notification> passwordChangedNotification = checkPasswordChanged(userFromDb, userFromU);
        Optional<Notification> rolesChangedNotification = checkRolesChanged(userFromDb, userFromU);

        emailChangedNotification.ifPresent(notifications::add);
        usernameChangedNotification.ifPresent(notifications::add);
        passwordChangedNotification.ifPresent(notifications::add);
        rolesChangedNotification.ifPresent(notifications::add);

        return notifications;
    }

}
