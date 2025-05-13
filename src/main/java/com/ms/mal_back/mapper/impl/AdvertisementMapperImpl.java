package com.ms.mal_back.mapper.impl;

import com.ms.mal_back.config.UrlBuilder;
import com.ms.mal_back.dto.AdvertisementRequest;
import com.ms.mal_back.dto.AdvertisementResponse;
import com.ms.mal_back.dto.AdvertisementSingularResponse;
import com.ms.mal_back.entity.*;
import com.ms.mal_back.entity.enums.Priority;
import com.ms.mal_back.entity.enums.Region;
import com.ms.mal_back.mapper.AdvertisementMapper;
import com.ms.mal_back.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdvertisementMapperImpl implements AdvertisementMapper {

    private final UrlBuilder urlBuilder;

    @Override
    public Advertisement toEntity(AdvertisementRequest request, User seller) {
        Advertisement ad = new Advertisement();
        ad.setAnimal(request.getAnimal());
        ad.setBreed(request.getBreed());
        ad.setAge(request.getAge());
        ad.setRegion(Region.fromDisplayName(request.getRegion()));
        ad.setDescription(request.getDescription());
        ad.setPrice(request.getPrice());
        ad.setSeller(seller);
        ad.setPriority(Priority.STANDARD);
        ad.setViewCount(0);
        return ad;
    }

    @Override
    public Advertisement updateEntity(Advertisement ad, AdvertisementRequest request) {
        ad.setAnimal(request.getAnimal());
        ad.setBreed(request.getBreed());
        ad.setAge(request.getAge());
        ad.setRegion(Region.fromDisplayName(request.getRegion()));
        ad.setDescription(request.getDescription());
        ad.setPrice(request.getPrice());
        ad.setLastModifiedAt(LocalDate.now());
        return ad;
    }

    @Override
    public AdvertisementSingularResponse toDto(Advertisement ad, boolean isFavorite, boolean isOwner) {
        List<String> photoUrls = ad.getPhotos().stream()
                .map(Photo::getFilePath)
                .map(urlBuilder::buildFullPhotoUrl)
                .toList();

        AdvertisementSingularResponse.SellerResponse seller = toSellerResponse(ad.getSeller());

        return new AdvertisementSingularResponse(
                ad.getId(),
                ad.getAnimal(),
                ad.getBreed(),
                ad.getAge(),
                ad.getRegion().getDisplayName(),
                ad.getDescription(),
                ad.getPrice(),
                ad.getPriority().getLabel(),
                ad.getViewCount(),
                ad.getCreatedAt(),
                ad.getLastModifiedAt(),
                isFavorite,
                photoUrls,
                seller,
                isOwner
        );
    }


    @Override
    public AdvertisementResponse toSimpleDto(Advertisement ad) {
        String firstPhoto = ad.getPhotos().isEmpty() ? null :
                urlBuilder.buildFullPhotoUrl(ad.getPhotos().get(0).getFilePath());

        return new AdvertisementResponse(
                ad.getId(),
                ad.getAnimal(),
                ad.getBreed(),
                ad.getRegion().getDisplayName(),
                ad.getPrice(),
                ad.getPriority().getLabel(),
                firstPhoto,
                ad.getAge(),
                ad.getCreatedAt()
        );
    }

    @Override
    public List<AdvertisementResponse> toSimpleDtoList(List<Advertisement> ads) {
        return ads.stream().map(this::toSimpleDto).collect(Collectors.toList());
    }

    public AdvertisementSingularResponse.SellerResponse toSellerResponse(User user) {
        return new AdvertisementSingularResponse.SellerResponse(
                user.getId(),
                user.getUsername(),
                user.getPhone(),
                user.getPhoto() != null ? urlBuilder.buildFullPhotoUrl(user.getPhoto().getFilePath()) : null
        );
    }
}
