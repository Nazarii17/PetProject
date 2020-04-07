package com.tkachuk.pet.constant;

public class ErrorMessage {
    public static final String USER_NOT_FOUND_BY_ID = "The user does not exist by this id: ";
    public static final String ORGANIZATION_NOT_FOUND_BY_ID = "The organization does not exist by this id: ";
    public static final String ORGANIZATION_PHOTO_NOT_FOUND_BY_ID = "The organization photo does not exist by this id: ";

    public static final String ORGANIZATION_NOT_SAVED = "Organization wasn't saved ";
    public static final String ORGANIZATION_PHOTO_NOT_SAVED = "Organization photo wasn't saved ";
    public static final String FILE_NOT_SAVED = "File wasn't saved ";

    public static final String FILE_IS_EMPTY = "File is empty ";
    public static final String FILE_NOT_VALID = "File is not valid ";
    public static final String FILE_NOT_IMAGE = "File is not an image ";
    public static final String FILE_NOT_CONVERTED = "File wasn't converted ";

    private ErrorMessage() {
    }
}
