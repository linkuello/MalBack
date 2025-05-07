package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.DeliveryOperatorResponse;
import com.ms.mal_back.dto.DeliveryRequestByCustomer;
import com.ms.mal_back.dto.DeliveryRequestBySeller;
import com.ms.mal_back.dto.DeliveryStatusResponse;
import com.ms.mal_back.entity.enums.DeliveryStatus;
import com.ms.mal_back.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final JwtService jwtService;

    @GetMapping("/requested-by")
    public ResponseEntity<List<DeliveryStatusResponse>> getDeliveriesRequestedBy(
            @RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(deliveryService.getDeliveriesRequestedBy(userId));
    }

    @GetMapping("/requested-from")
    public ResponseEntity<List<DeliveryStatusResponse>> getDeliveriesRequestedFrom(
            @RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(deliveryService.getDeliveriesRequestedFrom(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createDelivery(
            @RequestHeader("Authorization") String token,
            @RequestBody DeliveryRequestByCustomer request) {
        jwtService.validateToken(token);
        Long buyerId = jwtService.extractUserId(token);
        deliveryService.createDelivery(request, buyerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deny")
    public ResponseEntity<Void> sellerDeny(
            @RequestHeader("Authorization") String token,
            @RequestParam Long deliveryId) {
        jwtService.validateToken(token);
        Long sellerId = jwtService.extractUserId(token);
        deliveryService.sellerDenyRequest(deliveryId, sellerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> sellerConfirm(
            @RequestHeader("Authorization") String token,
            @RequestBody DeliveryRequestBySeller dto) {
        jwtService.validateToken(token);
        Long sellerId = jwtService.extractUserId(token);
        deliveryService.sellerConfirm(dto, sellerId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/operator/update-status")
    public ResponseEntity<Void> updateDeliveryStatusByOperator(
            @RequestParam Long deliveryId,
            @RequestParam String status) {
        // token auth check omitted
        DeliveryStatus parsedStatus;
        try {
            parsedStatus = DeliveryStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        deliveryService.operatorUpdateStatus(deliveryId, parsedStatus);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/operator/all")
    public ResponseEntity<List<DeliveryOperatorResponse>> getAllDeliveriesForOperator() {
        // token auth check omitted
        return ResponseEntity.ok(deliveryService.getDeliveriesForOperator());
    }
}

