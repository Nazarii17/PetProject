package com.tkachuk.pet.services;

import com.tkachuk.pet.entities.Photo;
import com.tkachuk.pet.repositories.PhotoRepo;
import com.tkachuk.pet.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PhotoService {

    @Value("${upload.path}")
    private String uploadPath;

    private final PhotoRepo photoRepo;

    @Autowired
    public PhotoService(PhotoRepo photoRepo) {
        this.photoRepo = photoRepo;
    }

    public Photo save(Photo photo) {
        return photoRepo.save(photo);
    }

    public void delete(long id) {
        photoRepo.deleteById(id);
    }

    public Photo getOne(Long id) {
        return photoRepo.getOne(id);
    }

    /**
     * @param id   - Id of old photo
     * @param logo - Given file which should be sett to an old Photo;
     * @return - Updated version of a Photo;
     * @throws IOException - File error;
     */
    public Photo update(Long id,
                        MultipartFile logo) throws IOException {
        Photo photo = getOne(id);
        if (FileUtil.isFileValid(logo)) {
            updatePhotoName(logo, photo);

        }
        return save(photo);
    }

    //TODO how to change?
    /**
     * Updates a name of a given Photo.
     * Takes a file from user. Changes name using UUID.
     * Saves the file to direction according to 'uploadPath'.
     * Then sets a new name to given Photo;
     *
     * @param file  - Given file from UI;
     * @param photo - Object here should be changed name;
     * @throws IOException - File error;
     */
    public void updatePhotoName(MultipartFile file, Photo photo) throws IOException {
        String resultFilename = FileUtil.getResultFilename(file, uploadPath);
        FileUtil.saveFile(uploadPath, file, resultFilename);
        photo.setName(resultFilename);
    }
}
