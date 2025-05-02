package com.ms.mal_back.dto;

import com.ms.mal_back.entity.enums.Priority;
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
public class AdvertisementResponse {
    private Long id;
    private String animal;
    private String breed;
    private String region;
    private BigDecimal price;
    private String priority;
    private String photoUrl;
    private LocalDate age;
    private LocalDate createdAt;
}

