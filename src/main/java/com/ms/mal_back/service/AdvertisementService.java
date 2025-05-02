package com.ms.mal_back.service;

import com.ms.mal_back.dto.AdvertisementCreatedResponse;
import com.ms.mal_back.dto.AdvertisementRequest;
import com.ms.mal_back.dto.AdvertisementResponse;
import com.ms.mal_back.dto.AdvertisementSingularResponse;
import com.ms.mal_back.entity.Advertisement;

import java.util.List;

public interface AdvertisementService {
    public AdvertisementSingularResponse getAdvertisementById(Long adId,Long currentUserId);
    List<AdvertisementResponse> getAllAdvertisements();
    List<AdvertisementResponse> getTopAdvertisements();
    List<AdvertisementResponse> getAdvertisementsByUser(Long userId);
    AdvertisementCreatedResponse createAdvertisement(Long userId, AdvertisementRequest request);
    AdvertisementCreatedResponse updateAdvertisement(Long adId, AdvertisementRequest request);
    List<String> getAllAnimals();
    List<String> getBreedsByAnimal(String animal);
    Advertisement getAdEntity(Long adId);
    void deleteAdById(Long adId,Long currentUserId);
}
