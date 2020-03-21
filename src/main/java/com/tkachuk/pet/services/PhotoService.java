package com.tkachuk.pet.services;

import com.tkachuk.pet.entities.OrganizationPhoto;
import com.tkachuk.pet.repositories.PhotoRepo;
import com.tkachuk.pet.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PhotoService {

    @Autowired
    @Qualifier("basePath")
    private String uploadPath;

    private final PhotoRepo photoRepo;

    @Autowired
    public PhotoService(PhotoRepo photoRepo) {
        this.photoRepo = photoRepo;
    }

    public OrganizationPhoto save(OrganizationPhoto organizationPhoto) {
        return photoRepo.save(organizationPhoto);
    }

    public void delete(long id) {
        photoRepo.deleteById(id);
    }

    public OrganizationPhoto getOne(Long id) {
        return photoRepo.getOne(id);
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
        String resultFilename = FileUtil.saveFile(uploadPath, file);
        organizationPhoto.setName(resultFilename);
    }
}
