package com.ms.mal_back.service;

import com.ms.mal_back.dto.AdvertisementResponse;

import java.util.List;

public interface FavoriteService {
    public void addFavorite(Long userId, Long advertisementId);
    public void removeFavorite(Long userId, Long advertisementId);
    public boolean isFavorite(Long userId, Long advertisementId);
    public List<AdvertisementResponse> getFavoritesForUser(Long userId);
}