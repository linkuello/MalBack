package com.ms.mal_back.dto;

import com.ms.mal_back.entity.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryOperatorResponse {
    private Long deliveryId;
    private Long adId;
    private String adAnimal;
    private String adBreed;

    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;

    private String sellerName;
    private String sellerPhone;
    private String sellerAddress;

    private DeliveryStatus status;
    private LocalDateTime createdAt;
}

