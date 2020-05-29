package com.hitachi.schedule.config.common;

import com.hitachi.schedule.config.exception.ErrorDownload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
public class FileReadUtil {


    public static String readFileName(MultipartFile imageFile) {
        String image_name = imageFile.getOriginalFilename();
        while (image_name.contains(GCConstGlobals.GS_PROP_FILE_PATH_SPLIT_FUGO)) {
            image_name = image_name.substring(image_name.indexOf(GCConstGlobals.GS_PROP_FILE_PATH_SPLIT_FUGO) + 1);
        }
        return image_name;
    }

    public static byte[] readFileData(MultipartFile imageFile) {
        byte[] imageFileData = null;
        try {
            InputStream imageFileIs = imageFile.getInputStream();
            imageFileData = new byte[(int) imageFile.getSize()];
            imageFileIs.read(imageFileData);
        } catch (Exception e) {
            throw new ErrorDownload();
        } finally {
            return imageFileData;
        }
    }
}
