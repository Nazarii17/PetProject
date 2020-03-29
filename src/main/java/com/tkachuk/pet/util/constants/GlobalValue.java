package com.tkachuk.pet.util.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalValue {
    public static String UPLOAD_PATH;

    @Value("${upload.path}")
    public void setUploadPath(String uploadPath) {
        UPLOAD_PATH = uploadPath;
    }
}
