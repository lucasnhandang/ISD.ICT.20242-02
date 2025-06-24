package com.hustict.aims.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;

@Service
public class SupabaseStorage implements ImageUploadStorage {
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.bucket}")
    private String bucket;

    @Value("${supabase.service-role-key}")
    private String serviceRole;

    private final RestTemplate restTemplate;

    public SupabaseStorage(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String upload(byte[] content, String fileName, String contentType) {
        String endpoint = supabaseUrl + "/storage/v1/object/" + bucket + "/" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceRole);
        headers.setContentType(MediaType.valueOf(contentType));

        HttpEntity<byte[]> request = new HttpEntity<>(content, headers);
        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.PUT, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + fileName;
        } else {
            throw new RuntimeException("Failed to upload: " + response.getStatusCode());
        }
    }
}

