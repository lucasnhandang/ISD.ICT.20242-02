package com.hustict.aims.service.product;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String upload(MultipartFile image);
}
