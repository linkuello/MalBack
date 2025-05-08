package com.ms.mal_back.service.impl;

import com.ms.mal_back.dto.AdvertisementCreatedResponse;
import com.ms.mal_back.dto.AdvertisementRequest;
import com.ms.mal_back.dto.AdvertisementResponse;
import com.ms.mal_back.dto.AdvertisementSingularResponse;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.entity.enums.Priority;
import com.ms.mal_back.mapper.AdvertisementMapper;
import com.ms.mal_back.repository.AdvertisementRepository;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.service.AdvertisementService;
import com.ms.mal_back.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final AdvertisementMapper advertisementMapper;
    private final FavoriteService favoriteService;

    @Override
    public AdvertisementSingularResponse getAdvertisementById(Long adId,Long currentUserId) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advertisement not found"));
        boolean isFavorite = favoriteService.isFavorite(currentUserId, ad.getId());
        ad.setViewCount(ad.getViewCount()+1);
        return advertisementMapper.toDto(ad,isFavorite);
    }

    @Override
    public List<AdvertisementResponse> getAllAdvertisements() {
        List<Advertisement> ads = advertisementRepository.findAll();

        return ads.stream()
                .sorted(Comparator.comparing(Advertisement::getPriority).reversed())
                .map(advertisementMapper::toSimpleDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvertisementResponse> getTopAdvertisements() {
        List<Advertisement> ads = advertisementRepository.findAll();

        List<Advertisement> prioritized = ads.stream()
                .sorted(Comparator
                        .comparing(Advertisement::getPriority).reversed()
                        .thenComparing(Advertisement::getViewCount, Comparator.reverseOrder()))
                .limit(30)
                .collect(Collectors.toList());

        return advertisementMapper.toSimpleDtoList(prioritized);
    }

    @Override
    public List<AdvertisementResponse> getAdvertisementsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Advertisement> ads = advertisementRepository.findBySeller(user);

        return advertisementMapper.toSimpleDtoList(ads);
    }

    @Override
    public AdvertisementCreatedResponse createAdvertisement(Long userId, AdvertisementRequest request) {
        User seller = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Advertisement ad = advertisementMapper.toEntity(request, seller);
        advertisementRepository.save(ad);

        return new AdvertisementCreatedResponse(ad.getId());
    }


    @Override
    public AdvertisementCreatedResponse updateAdvertisement(Long adId, AdvertisementRequest request) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advertisement not found"));

        advertisementMapper.updateEntity(ad, request);
        advertisementRepository.save(ad);

        return new AdvertisementCreatedResponse(ad.getId());
    }

    @Override
    public List<String> getAllAnimals() {
        return advertisementRepository.findDistinctAnimals().stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getBreedsByAnimal(String animal) {
        return advertisementRepository.findDistinctBreedsByAnimal(animal).stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    @Override
    public Advertisement getAdEntity(Long adId) {
        return advertisementRepository.findById(adId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Advertisement not found"));
    }

    @Override
    public void deleteAdById(Long adId,Long currentUserId){
        Advertisement ad = getAdEntity(adId);
        if (ad.getSeller().getId().equals(currentUserId)){
            advertisementRepository.delete(ad);
        }
    }

    @Scheduled(cron = "0 0 3 * * *") // Every day at 03:00
    public void resetExpiredPriorities() {
        LocalDate today = LocalDate.now();
        List<Advertisement> expiredAds = advertisementRepository
                .findByPriorityNotAndPriorityUntilBefore(Priority.STANDARD, today);

        for (Advertisement ad : expiredAds) {
            ad.setPriority(Priority.STANDARD);
            ad.setPriorityUntil(null);
        }

        advertisementRepository.saveAll(expiredAds);
    }
}
