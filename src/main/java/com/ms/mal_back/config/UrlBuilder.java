package com.ms.mal_back.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class UrlBuilder {

    public String buildFullPhotoUrl(String filePath) {
        if (filePath == null || filePath.isBlank()) return null;

        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString(); // https://your-backend.com

        return baseUrl + filePath; // → https://your-backend.com/photos/abc.jpg
    }
}
