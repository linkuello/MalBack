package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.AdvertisementResponse;
import com.ms.mal_back.service.FavoriteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final JwtService jwtService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{adId}")
    public ResponseEntity<Void> addFavorite(
            @RequestHeader("Authorization") String token,
            @PathVariable Long adId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        favoriteService.addFavorite(userId, adId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/remove/{adId}")
    public ResponseEntity<Void> removeFavorite(
            @RequestHeader("Authorization") String token,
            @PathVariable Long adId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        favoriteService.removeFavorite(userId, adId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/check/{adId}")
    public ResponseEntity<Boolean> isFavorite(
            @RequestHeader("Authorization") String token,
            @PathVariable Long adId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(favoriteService.isFavorite(userId, adId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<AdvertisementResponse>> getFavorites(
            @RequestHeader("Authorization") String token
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(favoriteService.getFavoritesForUser(userId));
    }
}
