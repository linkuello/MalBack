package com.ms.mal_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRequestByCustomer {
    private Long adId;
    private String phone;
    private String address;
}
