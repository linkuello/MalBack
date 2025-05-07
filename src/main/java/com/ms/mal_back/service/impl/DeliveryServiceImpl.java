package com.ms.mal_back.service.impl;

import com.ms.mal_back.dto.DeliveryOperatorResponse;
import com.ms.mal_back.dto.DeliveryRequestByCustomer;
import com.ms.mal_back.dto.DeliveryRequestBySeller;
import com.ms.mal_back.dto.DeliveryStatusResponse;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.Delivery;
import com.ms.mal_back.entity.enums.DeliveryStatus;
import com.ms.mal_back.mapper.DeliveryMapper;
import com.ms.mal_back.repository.AdvertisementRepository;
import com.ms.mal_back.repository.DeliveryRepository;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.service.DeliveryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AdvertisementRepository advertisementRepository;
    private final DeliveryMapper deliveryMapper;
    private final UserRepository userRepository;

    @Override
    public List<DeliveryStatusResponse> getDeliveriesRequestedBy(Long buyerId) {
        List<Delivery> deliveries = deliveryRepository.findByBuyerId(buyerId);
        Set<Long> adIds = deliveries.stream().map(Delivery::getAdId).collect(Collectors.toSet());
        Map<Long, Advertisement> adMap = advertisementRepository.findAllById(adIds)
                .stream().collect(Collectors.toMap(Advertisement::getId, Function.identity()));
        return deliveryMapper.toStatusResponsesForBuyer(deliveries, adMap);
    }

    @Override
    public List<DeliveryStatusResponse> getDeliveriesRequestedFrom(Long sellerId) {
        List<Delivery> deliveries = deliveryRepository.findBySellerId(sellerId);
        Set<Long> adIds = deliveries.stream().map(Delivery::getAdId).collect(Collectors.toSet());
        Map<Long, Advertisement> adMap = advertisementRepository.findAllById(adIds)
                .stream().collect(Collectors.toMap(Advertisement::getId, Function.identity()));
        return deliveryMapper.toStatusResponsesForSeller(deliveries, adMap);
    }

    @Override
    public void createDelivery(DeliveryRequestByCustomer request, Long buyerId) {
        Advertisement ad = advertisementRepository.findById(request.getAdId())
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));
        Long sellerId = ad.getSeller().getId();

        Delivery delivery = deliveryMapper.toEntityFromCustomer(request, buyerId, sellerId);
        delivery.setBuyerName(userRepository.getById(buyerId).getUsername());
        deliveryRepository.save(delivery);
    }

    @Override
    public void sellerDenyRequest(Long deliveryId, Long sellerId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found"));
        if (!delivery.getSellerId().equals(sellerId)) {
            throw new SecurityException("Access denied");
        }
        delivery.setStatus(DeliveryStatus.DENIED);
        deliveryRepository.save(delivery);
    }

    @Override
    public void sellerConfirm(DeliveryRequestBySeller dto, Long sellerId) {
        Delivery delivery = deliveryRepository.findById(dto.getDeliveryId())
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found"));
        if (!delivery.getSellerId().equals(sellerId)) {
            throw new SecurityException("Access denied");
        }
        deliveryMapper.applySellerResponse(delivery, dto);
        delivery.setSellerName(userRepository.getById(sellerId).getUsername());
        deliveryRepository.save(delivery);
    }

    @Override
    public void operatorUpdateStatus(Long id, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Delivery not found"));
        delivery.setStatus(status);
        deliveryRepository.save(delivery);
    }

    @Override
    public List<DeliveryOperatorResponse> getDeliveriesForOperator() {
        List<Delivery> deliveries = deliveryRepository.findByStatus(DeliveryStatus.READY);
        Set<Long> adIds = deliveries.stream().map(Delivery::getAdId).collect(Collectors.toSet());
        Map<Long, Advertisement> adMap = advertisementRepository.findAllById(adIds)
                .stream().collect(Collectors.toMap(Advertisement::getId, Function.identity()));
        return deliveryMapper.toOperatorResponses(deliveries, adMap);
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void deleteOldDeliveries() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        deliveryRepository.deleteByCreatedAtBefore(cutoff);
    }
}

