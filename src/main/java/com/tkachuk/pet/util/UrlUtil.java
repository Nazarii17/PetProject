package com.tkachuk.pet.util;

import com.tkachuk.pet.util.constants.FilePaths;

import java.io.File;

import static com.tkachuk.pet.util.constants.FilePaths.*;

public class UrlUtil {

    public static String getUrl(FilePaths filePaths) {
        String separator = File.separator;
        return separator + PROJECT_FOLDER_NAME.getValue() + separator + UPLOAD_PATH.getValue() + filePaths.getValue() + separator;
    }

    public static String getFilepath (FilePaths path){
        return UPLOAD_PATH.getValue() + path.getValue();
    }
}
