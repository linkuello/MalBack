package com.ms.mal_back.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class UrlBuilder {

    public String buildFullPhotoUrl(Long photoId) {
        if (photoId == null) return null;

        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString(); // e.g., https://your-backend.com

        return baseUrl + "/photos/" + photoId; // e.g., https://your-backend.com/photos/42
    }
}
