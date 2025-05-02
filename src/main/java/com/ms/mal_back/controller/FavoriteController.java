package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.AdvertisementResponse;
import com.ms.mal_back.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final JwtService jwtService;

    @Operation(summary = "Add advertisement to user's favorites")
    @PostMapping("/add/{adId}")
    public ResponseEntity<Void> addFavorite(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token,
            @PathVariable Long adId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);

        log.info("User {} is adding ad {} to favorites", userId, adId);
        favoriteService.addFavorite(userId, adId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove advertisement from user's favorites")
    @DeleteMapping("/{adId}")
    public ResponseEntity<Void> removeFavorite(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token,
            @PathVariable Long adId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);

        log.info("User {} is removing ad {} from favorites", userId, adId);
        favoriteService.removeFavorite(userId, adId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check if an advertisement is in user's favorites")
    @GetMapping("/check/{adId}")
    public ResponseEntity<Boolean> isFavorite(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token,
            @PathVariable Long adId
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);

        boolean isFav = favoriteService.isFavorite(userId, adId);
        log.debug("User {} favorite check for ad {} = {}", userId, adId, isFav);
        return ResponseEntity.ok(isFav);
    }

    @Operation(summary = "Get all advertisements in user's favorites")
    @GetMapping
    public ResponseEntity<List<AdvertisementResponse>> getFavorites(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token
    ) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);

        log.info("Fetching favorite ads for user {}", userId);
        return ResponseEntity.ok(favoriteService.getFavoritesForUser(userId));
    }
}
