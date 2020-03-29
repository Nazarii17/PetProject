package com.tkachuk.pet.util.constants;

import com.tkachuk.pet.provider.PropertiesProvider;

public enum FilePaths {

    UPLOAD_PATH(GlobalValue.UPLOAD_PATH),
    ORGANIZATION_LOGO_FILEPATH(PropertiesProvider.getProperty("upload.path.organization.logos")),
    ORGANIZATION_PHOTOS_FILEPATH(PropertiesProvider.getProperty("upload.path.organization.photos")),
    PROJECT_FOLDER_NAME(PropertiesProvider.getProperty("project.folder.name"));

    private String value;

    FilePaths(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
