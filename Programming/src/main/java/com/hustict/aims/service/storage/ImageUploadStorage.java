package com.hustict.aims.service.storage;

public interface ImageUploadStorage {
    String upload(byte[] content, String fileName, String contentType);
}
