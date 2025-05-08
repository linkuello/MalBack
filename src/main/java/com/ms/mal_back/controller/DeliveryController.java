package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.DeliveryRequestByCustomer;
import com.ms.mal_back.dto.DeliveryRequestBySeller;
import com.ms.mal_back.dto.DeliveryStatusResponse;
import com.ms.mal_back.service.DeliveryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final JwtService jwtService;
    private final DeliveryService deliveryService;

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
        Long userId = jwtService.extractUserId(token);
        deliveryService.createDelivery(request, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deny")
    public ResponseEntity<Void> denyDelivery(
            @RequestHeader("Authorization") String token,
            @RequestParam("deliveryId") Long deliveryId) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        deliveryService.sellerDenyRequest(deliveryId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmDelivery(
            @RequestHeader("Authorization") String token,
            @RequestBody DeliveryRequestBySeller dto) {
        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        deliveryService.sellerConfirm(dto, userId);
        return ResponseEntity.ok().build();
    }
}
