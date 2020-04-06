package com.tkachuk.pet.util;

import com.tkachuk.pet.constant.ErrorMessage;
import com.tkachuk.pet.exception.FileException;
import com.tkachuk.pet.exception.NotSavedException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {

    /**
     * Checks if file is given file not empty;
     *
     * @param file - given file {@link MultipartFile}
     * @return - 'true' if file is valid;
     */
    public static boolean isFileEmpty(MultipartFile file) {
        return file != null && !StringUtils.isEmpty(file.getOriginalFilename());
    }

    /**
     * Checks if file is given file is an image;
     *
     * @param file - given file {@link MultipartFile}
     * @return - 'true' if file is valid;
     */
    public static boolean isImage(MultipartFile file) {

        File f = convert(file);
        String mimeType = new MimetypesFileTypeMap().getContentType(f);
        String type = mimeType.split("/")[0];
        return type.equals("image");
    }

    /**
     * Validates a given image. If image not valid - throws exceptions;
     *
     * @param image - given file {@link MultipartFile}
     */
    public static void validateImage(MultipartFile image) {
        if (!FileUtil.isFileEmpty(image)) {
            throw new FileException(ErrorMessage.FILE_IS_EMPTY);
        } else if (!isImage(image)) {
            throw new FileException(ErrorMessage.FILE_NOT_IMAGE);
        }
    }

    /**
     * Converts a given {@link MultipartFile} to {@link File}
     *
     * @param file - given file {@link MultipartFile}
     * @return - {@link File}
     */
    private static File convert(MultipartFile file) {
        File toReturn = new File(file.getOriginalFilename());
        try {
            toReturn.createNewFile();
            FileOutputStream fos = new FileOutputStream(toReturn);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            throw new FileException(ErrorMessage.FILE_NOT_CONVERTED);
        }
        return toReturn;
    }

    /**
     * Creates a File name with {@link UUID#randomUUID()} on the beginning;
     *
     * @param file - given file {@link MultipartFile};
     * @return - encoded {@link String} name;
     */
    public static String getFilename(MultipartFile file) {
        String uuidFile = UUID.randomUUID().toString();
        return uuidFile + "." + file.getOriginalFilename();
    }

    /**
     * Saves a given file to a given directory;
     *
     * @param uploadPath - location where file should be saved on;
     * @param file       - given file {@link MultipartFile};
     * @return - {@link String} resultFilename(encoded name of saved file);
     */
    public static String saveFile(String uploadPath, MultipartFile file) {

        String resultFilename = FileUtil.getFilename(file);
        File fileToSave = new File(uploadPath + File.separator + resultFilename);
        try {
            file.transferTo(fileToSave);
        } catch (IOException e) {
            String message = ErrorMessage.FILE_NOT_SAVED + file.getName() + ". " + e.getMessage();
            e.printStackTrace();
            //TODO log.error(message, e);
            throw new NotSavedException(message);
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
    public static String saveImage(String uploadPath, MultipartFile file) {
        validateImage(file);
        return FileUtil.saveFile(uploadPath, file);
    }
}
