package com.ms.mal_back.dto;

import com.ms.mal_back.entity.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryStatusResponse {
    private Long deliveryId;
    private Long adId;
    private String adAnimal;
    private String adBreed;
    private DeliveryStatus status;
}

