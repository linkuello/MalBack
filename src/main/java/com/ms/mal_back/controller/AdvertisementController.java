package com.ms.mal_back.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.*;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.service.AdvertisementService;
import com.ms.mal_back.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ad")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final PhotoService photoService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{adId}")
    public AdvertisementSingularResponse getAdById(
            @PathVariable Long adId,
            @RequestHeader(value = "Authorization", required = false) String token) {

        Long userId = (token != null && jwtService.isTokenValid(token))
                ? jwtService.extractUserId(token)
                : null;

        return advertisementService.getAdvertisementById(adId, userId);
    }

    @GetMapping("/all-ads")
    public List<AdvertisementResponse> getAllAds() {
        return advertisementService.getAllAdvertisements();
    }

    @GetMapping("/top-ads")
    public List<AdvertisementResponse> getTopAds() {
        return advertisementService.getTopAdvertisements();
    }

    @GetMapping("/user-ads/{userId}")
    public List<AdvertisementResponse> getAdsByUser(@PathVariable Long userId) {
        return advertisementService.getAdvertisementsByUser(userId);
    }

    @GetMapping("/animals")
    public List<String> getAllAnimals() {
        return advertisementService.getAllAnimals();
    }

    @GetMapping("/breeds/{animal}")
    public List<String> getBreedsByAnimal(@PathVariable String animal) {
        return advertisementService.getBreedsByAnimal(animal);
    }

    @GetMapping("/my-ads")
    @PreAuthorize("isAuthenticated()")
    public List<AdvertisementResponse> getMyAds(@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return advertisementService.getAdvertisementsByUser(userId);
    }

    @DeleteMapping("/{adId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteAd(
            @PathVariable Long adId,
            @RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        advertisementService.deleteAdById(adId, userId);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new advertisement with photos")
    public ResponseEntity<AdvertisementCreatedResponse> createAd(
            @Parameter(hidden = true)
            @RequestHeader("Authorization") String token,

            @Parameter(
                    description = "Advertisement form data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdvertisementRequest.class)
                    )
            )
            @RequestPart("formData") String formDataJson,

            @Parameter(
                    description = "List of photo files",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            array = @ArraySchema(schema = @Schema(type = "string", format = "binary"))
                    )
            )
            @RequestPart("photos") List<MultipartFile> photos
    ) throws JsonProcessingException {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);

        AdvertisementRequest request = objectMapper.readValue(formDataJson, AdvertisementRequest.class);

        AdvertisementCreatedResponse response = advertisementService.createAdvertisement(userId, request);
        if (photos != null && !photos.isEmpty()) {
            Advertisement ad = advertisementService.getAdEntity(response.getId());
            photoService.replaceAdPhotos(ad, photos);
        }

        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/{adId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an advertisement with new photos")
    public ResponseEntity<AdvertisementCreatedResponse> updateAd(
            @PathVariable Long adId,
            @RequestHeader("Authorization") String token,

            @Parameter(
                    description = "Advertisement form data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdvertisementRequest.class)
                    )
            )
            @RequestPart("formData") String formDataJson,
            @RequestPart("replacePhotos") Boolean replacePhotos,
            @Parameter(
                    description = "List of photo files",
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            array = @ArraySchema(schema = @Schema(type = "string", format = "binary"))
                    )
            )
            @RequestPart("photos") List<MultipartFile> photos
    ) throws JsonProcessingException {
        jwtService.validateToken(token);
        AdvertisementRequest request = objectMapper.readValue(formDataJson, AdvertisementRequest.class);

        AdvertisementCreatedResponse response = advertisementService.updateAdvertisement(adId, request);
        Advertisement ad = advertisementService.getAdEntity(response.getId());
        if (photos != null && !photos.isEmpty()) {
            if (Boolean.TRUE.equals(replacePhotos)) {
                photoService.replaceAdPhotos(ad, photos); // delete old, save new
            } else {
                photoService.appendAdPhotos(ad, photos); // preserve old, add new
            }
        }

        return ResponseEntity.ok(response);
    }
}
