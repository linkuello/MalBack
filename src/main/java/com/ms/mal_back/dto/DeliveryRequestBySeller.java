package com.ms.mal_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRequestBySeller {
    private Long deliveryId;
    private String phone;
    private String address;
}
