package com.tkachuk.pet.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@Slf4j
public class FileStorageConfig {

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
        File baseDir = getFile("users", "profile-photos");
        return baseDir.getAbsolutePath();
    }

    @Bean(name = "uploadUserPhotosFilepath")
    public String userPhotosFilepath() {
        File baseDir = getFile("users", "photos");
        return baseDir.getAbsolutePath();
    }

    @Bean(name = "uploadOrganizationLogoFilepath")
    public String organizationLogoFilepath() {
        File baseDir = getFile("organizations", "logos");
        return baseDir.getAbsolutePath();
    }

    @Bean(name = "uploadOrganizationPhotosFilepath")
    public String organizationPhotosFilepath() {
        File baseDir = getFile("organizations", "photos");
        return baseDir.getAbsolutePath();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Bean(name = "filepathUserProfilePhoto")
    public String filepathUserProfilePhoto() {
        return getUrl("users", "profile-photos");
    }

    @Bean(name = "filepathUserPhotos")
    public String filepathUserPhotos() {
        return getUrl("users", "photos");
    }

    @Bean(name = "filepathOrganizationLogo")
    public String filepathOrganizationLogo() {
        return getUrl("organizations", "logos");
    }

    @Bean(name = "filepathOrganizationPhotos")
    public String filepathOrganizationPhotos() {
        return getUrl("organizations", "photos");
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public File getFile(String... folderNames) {
        File baseDir = new File(getFilepath(folderNames));
        if (!baseDir.exists()) {
            baseDir.mkdirs();
            log.info(baseDir.getName() + " '" + baseDir.getAbsolutePath() + "' created");
        }
        return baseDir;
    }

    public String getFilepath(String... folderNames) {
        StringBuilder filepath = new StringBuilder(File.separator);
        for (String s : folderNames) {
            filepath.append(s).append(File.separator);
        }
        return uploadPath + filepath;
    }

    public String getUrl(String... folderNames) {
        StringBuilder filepath = new StringBuilder(File.separator);
        for (String s : folderNames) {
            filepath.append(s).append(File.separator);
        }
        return File.separator + uploadPathRoot + File.separator + uploadPath + filepath;
    }
}
