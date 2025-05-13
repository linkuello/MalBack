package com.ms.mal_back.service.impl;

import com.ms.mal_back.dto.AdvertisementResponse;
import com.ms.mal_back.entity.Favorite;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.mapper.AdvertisementMapper;
import com.ms.mal_back.repository.FavoriteRepository;
import com.ms.mal_back.repository.AdvertisementRepository;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper advertisementMapper;

    @Override
    public void addFavorite(Long userId, Long advertisementId) {
        Advertisement ad = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advertisement not found"));

        if (ad.getSeller().getId().equals(userId)) {
            throw new IllegalArgumentException("You cannot favorite your own ad");
        }

        if (favoriteRepository.existsByUserIdAndAdvertisementId(userId, ad.getId())) {
            throw new IllegalStateException("This ad is already in your favorites");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setAdvertisement(ad);
        favoriteRepository.save(favorite);
    }

    @Override
    public void removeFavorite(Long userId, Long advertisementId) {
        favoriteRepository.deleteByUserIdAndAdId(userId, advertisementId);
    }

    @Override
    public boolean isFavorite(Long userId, Long advertisementId) {
        return favoriteRepository.existsByUserIdAndAdvertisementId(userId, advertisementId);
    }

    @Override
    public List<AdvertisementResponse> getFavoritesForUser(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

        Set<Long> adIds = favorites.stream()
                .map(fav -> fav.getAdvertisement().getId())
                .collect(Collectors.toSet());

        Set<Long> existingAdIds = advertisementRepository.findAllById(adIds).stream()
                .map(Advertisement::getId)
                .collect(Collectors.toSet());

        List<Favorite> validFavorites = favorites.stream()
                .filter(fav -> existingAdIds.contains(fav.getAdvertisement().getId()))
                .toList();

        List<Favorite> toDelete = favorites.stream()
                .filter(fav -> !existingAdIds.contains(fav.getAdvertisement().getId()))
                .toList();

        if (!toDelete.isEmpty()) {
            favoriteRepository.deleteAll(toDelete);
        }

        List<Advertisement> ads = validFavorites.stream()
                .map(Favorite::getAdvertisement)
                .toList();

        return advertisementMapper.toSimpleDtoList(ads);
    }
}
