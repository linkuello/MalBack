package com.ms.mal_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementSingularResponse {
    private Long id;
    private String animal;
    private String breed;
    private LocalDate age;
    private String region;
    private String description;
    private BigDecimal price;
    private String priority;
    private int viewCount;
    private LocalDate createdAt;
    private LocalDate lastModifiedAt;
    private boolean isFavorite;
    private List<String> photoUrls;
    private SellerResponse seller;
    private boolean isOwner;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellerResponse {
        private Long id;
        private String name;
        private String phone;
        private String photoUrl;
    }
}
