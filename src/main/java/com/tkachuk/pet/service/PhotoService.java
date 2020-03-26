package com.tkachuk.pet.service;

import com.tkachuk.pet.entity.OrganizationPhoto;
import com.tkachuk.pet.entity.UserPhoto;
import com.tkachuk.pet.repository.OrganizationPhotoRepo;
import com.tkachuk.pet.repository.UserPhotoRepo;
import com.tkachuk.pet.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PhotoService {

    @Autowired
    @Qualifier("basePath")
    private String uploadPath;

    @Autowired
    @Qualifier("userPhotosBasePath")
    private String userPhotosBasePath;

    private final OrganizationPhotoRepo organizationPhotoRepo;
    private final UserPhotoRepo userPhotoRepo;

    @Autowired
    public PhotoService(OrganizationPhotoRepo organizationPhotoRepo, UserPhotoRepo userPhotoRepo) {
        this.organizationPhotoRepo = organizationPhotoRepo;
        this.userPhotoRepo = userPhotoRepo;
    }

    public OrganizationPhoto save(OrganizationPhoto organizationPhoto) {
        return organizationPhotoRepo.save(organizationPhoto);
    }

    public UserPhoto save(UserPhoto userPhoto) {
        return userPhotoRepo.save(userPhoto);
    }

    public void delete(long id) {
        organizationPhotoRepo.deleteById(id);
    }

    public OrganizationPhoto getOne(Long id) {
        return organizationPhotoRepo.getOne(id);
    }

    public UserPhoto getOneUserPhoto(Long id) {
        return userPhotoRepo.getOne(id);
    }

    /**
     * @param id   - Id of old photo
     * @param logo - Given file which should be sett to an old Photo;
     * @return - Updated version of a Photo;
     * @throws IOException - File error;
     */
    public OrganizationPhoto update(Long id,
                                    MultipartFile logo) throws IOException {
        OrganizationPhoto organizationPhoto = getOne(id);
        if (FileUtil.isFileValid(logo)) {
            updatePhotoName(logo, organizationPhoto);

        }
        return save(organizationPhoto);
    }

    /**
     * @param id   - Id of old photo
     * @param logo - Given file which should be sett to an old Photo;
     * @return - Updated version of a Photo;
     * @throws IOException - File error;
     */
    public UserPhoto updateUserPhoto(Long id,
                                    MultipartFile logo) throws IOException {
        UserPhoto userPhoto = getOneUserPhoto(id);
        if (FileUtil.isFileValid(logo)) {
            updatePhotoName(logo, userPhoto);
        }
        return save(userPhoto);
    }

    //TODO I don't like the method's name.
    /**
     * Updates a name of a given Photo.
     * Takes a file from user. Changes name using UUID.
     * Saves the file to direction according to 'uploadPath'.
     * Then sets a new name to given Photo;
     *
     * @param file  - Given file from UI;
     * @param organizationPhoto - Object here should be changed name;
     * @throws IOException - File error;
     */
    public void updatePhotoName(MultipartFile file, OrganizationPhoto organizationPhoto) throws IOException {
        String resultFilename = FileUtil.saveFile(userPhotosBasePath, file);
        organizationPhoto.setName(resultFilename);
    }

    /**
     * Updates a name of a given Photo.
     * Takes a file from user. Changes name using UUID.
     * Saves the file to direction according to 'uploadPath'.
     * Then sets a new name to given Photo;
     *
     * @param file  - Given file from UI;
     * @param userPhoto - Object here should be changed name;
     * @throws IOException - File error;
     */
    public void updatePhotoName(MultipartFile file, UserPhoto userPhoto) throws IOException {
        String resultFilename = FileUtil.saveFile(userPhotosBasePath, file);
        userPhoto.setName(resultFilename);
    }

    public void deleteUserPhoto(long id) {
        userPhotoRepo.deleteById(id);
    }
}
