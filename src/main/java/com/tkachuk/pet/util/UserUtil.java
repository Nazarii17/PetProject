package com.tkachuk.pet.util;

import com.tkachuk.pet.entity.Role;
import com.tkachuk.pet.entity.User;

import java.util.Set;

public class UserUtil {

    public static boolean isEmailChanged(User userFromUi, String userEmail) {
        return (userFromUi.getEmail() != null && !userFromUi.getEmail().equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(userFromUi.getEmail()));
    }

    public static boolean isUsernameChanged(User userFromUi, String username) {
        return (userFromUi.getUsername() != null && !userFromUi.getUsername().equals(username)) ||
                (username != null && !username.equals(userFromUi.getUsername()));
    }

    public static boolean areRolesChanged(User userFromUi, Set<Role> roles) {
        return !roles.containsAll(userFromUi.getRoles()) || !userFromUi.getRoles().containsAll(roles);
    }
}
