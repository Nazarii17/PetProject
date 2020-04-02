package com.tkachuk.pet.util;

import com.tkachuk.pet.dto.UserProfileDto;
import com.tkachuk.pet.entity.Gender;
import com.tkachuk.pet.entity.Role;
import com.tkachuk.pet.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

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

    public static boolean isGenderChanged(User userFromUi, Gender gender) {
        return (userFromUi.getGender() != null && !userFromUi.getGender().equals(gender)) ||
                (gender != null && !gender.equals(userFromUi.getGender()));
    }

    public static boolean isProfilePhotoEmpty(UserProfileDto userProfileDto) {
        return userProfileDto.getProfilePhoto() != null;
    }

    public static boolean isUsernameValid(User userFromDb, User userFromUi) {
        return UserUtil.isUsernameChanged(userFromUi, userFromDb.getUsername()) && !StringUtils.isEmpty(userFromUi.getUsername());
    }

    public static boolean areRolesValid(User userFromDb, User userFromUi) {
        return UserUtil.areRolesChanged(userFromUi, userFromDb.getRoles()) && userFromUi.getRoles().size() > 0;
    }

    public static boolean isPasswordValid(User userFromDb, User userFromUi, PasswordEncoder passwordEncoder) {
        return !passwordEncoder.matches(userFromUi.getPassword(), userFromDb.getPassword()) && !StringUtils.isEmpty(userFromUi.getPassword());
    }

    public static boolean isEmailValid(User userFromDb, User userFromUi) {
        return UserUtil.isEmailChanged(userFromUi, userFromDb.getEmail()) && !StringUtils.isEmpty(userFromUi.getEmail());
    }
}
