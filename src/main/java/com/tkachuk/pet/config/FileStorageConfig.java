package com.tkachuk.pet.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@Slf4j
public class FileStorageConfig {

    private static final String DIR_USER = "users";
    private static final String DIR_USER_PROFILE_PHOTO = "profile-photos";
    private static final String DIR_USER_PHOTOS = "photos";
    private static final String DIR_ORGANIZATION = "organizations";
    private static final String DIR_ORGANIZATION_LOGO = "logos";
    private static final String DIR_ORGANIZATION_PHOTOS = "photos";

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.path.root}")
    private String uploadPathRoot;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Bean(name = "uploadPath")
    public String uploadPath() {
        return uploadPath;
    }

    @Bean(name = "uploadUserProfilePhotoFilepath")
    public String userProfilePhotoFilepath() {
        File baseDir = getFile(DIR_USER, DIR_USER_PROFILE_PHOTO);
        return baseDir.getAbsolutePath();
    }

    @Bean(name = "uploadUserPhotosFilepath")
    public String userPhotosFilepath() {

        File baseDir = getFile(DIR_USER, DIR_USER_PHOTOS);
        return baseDir.getAbsolutePath();
    }

    @Bean(name = "uploadOrganizationLogoFilepath")
    public String organizationLogoFilepath() {
        File baseDir = getFile(DIR_ORGANIZATION, DIR_ORGANIZATION_LOGO);
        return baseDir.getAbsolutePath();
    }

    @Bean(name = "uploadOrganizationPhotosFilepath")
    public String organizationPhotosFilepath() {
        File baseDir = getFile(DIR_ORGANIZATION, DIR_ORGANIZATION_PHOTOS);
        return baseDir.getAbsolutePath();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Bean(name = "filepathUserProfilePhoto")
    public String filepathUserProfilePhoto() {
        return getUrl(DIR_USER, DIR_USER_PROFILE_PHOTO);
    }

    @Bean(name = "filepathUserPhotos")
    public String filepathUserPhotos() {
        return getUrl(DIR_USER, DIR_USER_PHOTOS);
    }

    @Bean(name = "filepathOrganizationLogo")
    public String filepathOrganizationLogo() {
        return getUrl(DIR_ORGANIZATION, DIR_ORGANIZATION_LOGO);
    }

    @Bean(name = "filepathOrganizationPhotos")
    public String filepathOrganizationPhotos() {
        return getUrl(DIR_ORGANIZATION, DIR_ORGANIZATION_PHOTOS);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public File getFile(String... folderNames) {
        File baseDir = new File(getUploadPath(folderNames));
        if (!baseDir.exists()) {
            baseDir.mkdirs();
            log.info(baseDir.getName() + " '" + baseDir.getAbsolutePath() + "' created");
        }
        return baseDir;
    }

    public String getUploadPath(String... folderNames) {
        StringBuilder filepath = getFilepath(folderNames);
        return uploadPath + filepath;
    }

    public String getUrl(String... folderNames) {
        StringBuilder filepath = getFilepath(folderNames);
        return File.separator + uploadPathRoot + File.separator + uploadPath + filepath;
    }

    public StringBuilder getFilepath(String[] folderNames) {
        StringBuilder filepath = new StringBuilder(File.separator);
        for (String s : folderNames) {
            filepath.append(s).append(File.separator);
        }
        return filepath;
    }
}
