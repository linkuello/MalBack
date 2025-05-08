package com.ms.mal_back.mapper.impl;

import com.ms.mal_back.dto.DeliveryOperatorResponse;
import com.ms.mal_back.dto.DeliveryRequestByCustomer;
import com.ms.mal_back.dto.DeliveryRequestBySeller;
import com.ms.mal_back.dto.DeliveryStatusResponse;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.Delivery;
import com.ms.mal_back.entity.enums.DeliveryStatus;
import com.ms.mal_back.mapper.DeliveryMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DeliveryMapperImpl implements DeliveryMapper {

    @Override
    public Delivery toEntityFromCustomer(DeliveryRequestByCustomer dto, Long buyerId, Long sellerId) {
        Delivery delivery = new Delivery();
        delivery.setAdId(dto.getAdId());
        delivery.setBuyerId(buyerId);
        delivery.setSellerId(sellerId);
        delivery.setBuyerPhone(dto.getPhone());
        delivery.setBuyerAddress(dto.getAddress());
        delivery.setStatus(DeliveryStatus.NEW);
        return delivery;
    }

    @Override
    public void applySellerResponse(Delivery delivery, DeliveryRequestBySeller dto) {
        delivery.setSellerPhone(dto.getPhone());
        delivery.setSellerAddress(dto.getAddress());
        delivery.setStatus(DeliveryStatus.READY);
    }

    @Override
    public List<DeliveryStatusResponse> toStatusResponsesForSeller(List<Delivery> deliveries, Map<Long, Advertisement> adMap) {
        return deliveries.stream()
                .map(d -> {
                    Advertisement ad = adMap.get(d.getAdId());
                    return new DeliveryStatusResponse(
                            d.getId(),
                            d.getAdId(),
                            ad.getAnimal(),
                            ad.getBreed(),
                            d.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryStatusResponse> toStatusResponsesForBuyer(List<Delivery> deliveries, Map<Long, Advertisement> adMap) {
        return deliveries.stream()
                .map(d -> {
                    Advertisement ad = adMap.get(d.getAdId());
                    return new DeliveryStatusResponse(
                            d.getId(),
                            d.getAdId(),
                            ad.getAnimal(),
                            ad.getBreed(),
                            d.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryOperatorResponse> toOperatorResponses(List<Delivery> deliveries, Map<Long, Advertisement> adMap) {
        return deliveries.stream()
                .map(d -> {
                    Advertisement ad = adMap.get(d.getAdId());
                    return new DeliveryOperatorResponse(
                            d.getId(),
                            d.getAdId(),
                            ad.getAnimal(),
                            ad.getBreed(),
                            d.getBuyerName(),
                            d.getBuyerPhone(),
                            d.getBuyerAddress(),
                            d.getSellerName(),
                            d.getSellerPhone(),
                            d.getSellerAddress(),
                            d.getStatus(),
                            d.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
    }
}
