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

    public static void saveFile(String uploadPath, MultipartFile file, String resultFilename) throws IOException {
        file.transferTo(new File(uploadPath + File.separator + resultFilename));
    }

    public static String getResultFilename(MultipartFile file, String uploadPath) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        return FileUtil.getFilename(file);
    }
}
