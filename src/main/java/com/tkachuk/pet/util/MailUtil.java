package com.tkachuk.pet.util;

public class MailUtil {
    public static String getSubject(Notifications notification) {
        return notification.name();
    }

    public static String getMessage(Notifications updatedName) {
        switch (updatedName) {
            case GENDER_UPDATED:
                return Messages.GENDER_UPDATED_MESSAGE;
            case NAME_UPDATED_BY_ADMINISTRATION:
                return Messages.NAME_UPDATED_MESSAGE_BY_ADMINISTRATION;
            case ROLE_UPDATED_BY_ADMINISTRATION:
                return Messages.ROLE_UPDATED_MESSAGE_BY_ADMINISTRATION;
            case EMAIL_UPDATED_BY_ADMINISTRATION:
                return Messages.EMAIL_UPDATED_MESSAGE_BY_ADMINISTRATION;
            case PASSWORD_UPDATED_BY_ADMINISTRATION:
                return Messages.PASSWORD_UPDATED_MESSAGE_BY_ADMINISTRATION;
            case ROLE_UPDATED:
                return Messages.ROLE_UPDATED_MESSAGE;
            case EMAIL_UPDATED:
                return Messages.EMAIL_UPDATED_MESSAGE;
            case PASSWORD_UPDATED:
                return Messages.PASSWORD_UPDATED_MESSAGE;
            case PROFILE_PHOTO_UPDATED:
                return Messages.PROFILE_PHOTO_UPDATED_MESSAGE;
            case NAME_UPDATED:
                return Messages.NAME_UPDATED_MESSAGE;
        }
        return "Informational message!";
    }
}
