package com.hustict.aims.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class ImageUtils {
    public static String generateUniqueFilename(MultipartFile image) {
        String original = StringUtils.cleanPath(image.getOriginalFilename() != null ? image.getOriginalFilename() : "image");
        return UUID.randomUUID().toString() + "_" + original;
    }
}
