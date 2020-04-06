package com.tkachuk.pet.constant;

public enum Notification {
    ACTIVATION_CODE("Activation code"),
    MESSAGE_BEGINNING("Dear customer, your "),
    PROFILE_UPDATED("Dear customer, your profile was successfully changed!"),
    NAME_UPDATED("Dear customer, your name was successfully changed!"),
    PROFILE_PHOTO_UPDATED("Dear customer, your profile photo was successfully changed!"),
    PASSWORD_UPDATED("Dear customer, your password was successfully changed!"),
    EMAIL_UPDATED("Dear customer, your email was successfully changed!"),
    ROLE_UPDATED("Dear customer, your role was successfully changed!"),
    GENDER_UPDATED("Dear customer, your gender was successfully changed!"),
    EMAIL_UPDATED_BY_ADMINISTRATION("Dear customer, your email was successfully changed by administration!"),
    PASSWORD_UPDATED_BY_ADMINISTRATION("Dear customer, your password was successfully changed by administration!"),
    NAME_UPDATED_BY_ADMINISTRATION("Dear customer, your name was successfully changed by administration!"),
    ROLE_UPDATED_BY_ADMINISTRATION("Dear customer, your role was successfully changed by administration!");

    private String value;

    Notification(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
