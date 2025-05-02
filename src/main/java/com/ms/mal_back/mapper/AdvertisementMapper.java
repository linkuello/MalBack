package com.ms.mal_back.mapper;

import com.ms.mal_back.dto.AdvertisementRequest;
import com.ms.mal_back.dto.AdvertisementResponse;
import com.ms.mal_back.dto.AdvertisementSingularResponse;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.User;

import java.util.List;

public interface AdvertisementMapper {
    public Advertisement toEntity(AdvertisementRequest request, User seller);
    public Advertisement updateEntity(Advertisement ad, AdvertisementRequest request);
    public AdvertisementSingularResponse toDto(Advertisement ad,boolean isFavorite);
    public AdvertisementResponse toSimpleDto(Advertisement ad);
    public List<AdvertisementResponse> toSimpleDtoList(List<Advertisement> ads);
}
