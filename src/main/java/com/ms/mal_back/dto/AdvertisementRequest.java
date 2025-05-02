package com.ms.mal_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementRequest {
    private String animal;
    private String breed;
    private String region;
    private LocalDate age;
    private String description;
    private BigDecimal price;
}
