package com.tkachuk.pet.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {
    public static boolean isFileValid(MultipartFile file) {
        return file != null && !file.getOriginalFilename().isEmpty();
    }

    public static String getFilename(MultipartFile file) {
        String uuidFile = UUID.randomUUID().toString();
        return uuidFile + "." + file.getOriginalFilename();
    }

    public static String saveFile(String uploadPath, MultipartFile file) throws IOException {
        String resultFilename = FileUtil.getFilename(file);
        File fileToSave = new File(uploadPath + File.separator + resultFilename);
        file.transferTo(fileToSave);
        return fileToSave.getPath();
    }

    public static String getResultFilename(MultipartFile file, String uploadPath) throws IOException {
        // uploadDir достатньо створити один раз, а цей код виконується кожен раз,
        // коли викликається метод OrganizationService.setLogo()
        // Тай метод називається getResultFilename(), а не createUploadDir()

        return FileUtil.getFilename(file);
    }
}
