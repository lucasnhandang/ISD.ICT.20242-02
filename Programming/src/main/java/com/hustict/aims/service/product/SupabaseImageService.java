package com.hustict.aims.service.product;

import com.hustict.aims.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.Objects;

@Service
public class SupabaseImageService implements ImageService {
    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.bucket}")
    private String bucket;

    @Value("${supabase.service-role-key}")
    private String serviceRole;

    private final RestTemplate restTemplate;

    public SupabaseImageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String upload(MultipartFile image) {
        try {
            String filename = ImageUtils.generateUniqueFilename(image);
            String endpoint = supabaseUrl + "/storage/v1/object/" + bucket + "/" + filename;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + serviceRole);
            headers.setContentType(MediaType.valueOf(Objects.requireNonNull(image.getContentType())));

            HttpEntity<byte[]> request = new HttpEntity<>(image.getBytes(), headers);
            ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.PUT, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + filename;
            } else {
                throw new RuntimeException("Failed to upload to Supabase: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image to Supabase: " + e.getMessage(), e);
        }
    }
}

