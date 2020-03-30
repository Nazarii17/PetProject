package com.tkachuk.pet.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {
    public static boolean isFileValid(MultipartFile file) {
        return file != null && !StringUtils.isEmpty(file.getOriginalFilename());
    }

    public static void validateFile(MultipartFile file) {
        if (!FileUtil.isFileValid(file)) {
            throw new RuntimeException("File " + file.getName() + " is not valid");
        }
    }

    public static String getFilename(MultipartFile file) {
        String uuidFile = UUID.randomUUID().toString();
        return uuidFile + "." + file.getOriginalFilename();
    }

    public static String saveFile(String uploadPath, MultipartFile file) {

        String resultFilename = FileUtil.getFilename(file);
        File fileToSave = new File(uploadPath + File.separator + resultFilename);
        try {
            file.transferTo(fileToSave);
        } catch (IOException e) {
            String message = "Could not to save file " + file.getName() + ". " + e.getMessage();
            e.printStackTrace();
            //TODO log.error(message, e);
            throw new RuntimeException(message, e);
        }
        return resultFilename;
    }

    /**
     * Saves if file is valid and returns an encoded name of the saved file;
     * If vile isn't valid - throws an exception;
     *
     * @param uploadPath - filepath here file should be saved;
     * @param file       - file which should be saved;
     * @return - encoded filename or an exception;
     */
    public static String savePhoto(String uploadPath, MultipartFile file) {

        if (FileUtil.isFileValid(file)) {
            return FileUtil.saveFile(uploadPath, file);
        } else {
            throw new RuntimeException("File is not valid");
        }
    }
}
