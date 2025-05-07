package com.ms.mal_back.mapper;

import com.ms.mal_back.dto.DeliveryOperatorResponse;
import com.ms.mal_back.dto.DeliveryRequestByCustomer;
import com.ms.mal_back.dto.DeliveryRequestBySeller;
import com.ms.mal_back.dto.DeliveryStatusResponse;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.Delivery;

import java.util.List;
import java.util.Map;

public interface DeliveryMapper {
    public Delivery toEntityFromCustomer(DeliveryRequestByCustomer dto, Long buyerId, Long sellerId);
    public void applySellerResponse(Delivery delivery, DeliveryRequestBySeller dto);
    public List<DeliveryStatusResponse> toStatusResponsesForSeller(List<Delivery> deliveries, Map<Long, Advertisement> adMap) ;
    public List<DeliveryStatusResponse> toStatusResponsesForBuyer(List<Delivery> deliveries, Map<Long, Advertisement> adMap);
    public List<DeliveryOperatorResponse> toOperatorResponses(List<Delivery> deliveries, Map<Long, Advertisement> adMap);
}