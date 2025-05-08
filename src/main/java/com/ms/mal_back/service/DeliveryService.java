package com.ms.mal_back.service;

import com.ms.mal_back.dto.DeliveryOperatorResponse;
import com.ms.mal_back.dto.DeliveryRequestByCustomer;
import com.ms.mal_back.dto.DeliveryRequestBySeller;
import com.ms.mal_back.dto.DeliveryStatusResponse;
import com.ms.mal_back.entity.enums.DeliveryStatus;

import java.util.List;

public interface DeliveryService {
    public List<DeliveryStatusResponse> getDeliveriesRequestedBy(Long buyerId);
    public List<DeliveryStatusResponse> getDeliveriesRequestedFrom(Long sellerId) ;
    public void createDelivery(DeliveryRequestByCustomer request, Long buyerId) ;
    public void sellerDenyRequest(Long deliveryId, Long sellerId) ;
    public void sellerConfirm(DeliveryRequestBySeller dto, Long sellerId) ;
    public void operatorUpdateStatus(Long id, DeliveryStatus status) ;
    public List<DeliveryOperatorResponse> getDeliveriesForOperator();
    public void deleteAllByUserId(Long userId);
}