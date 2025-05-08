package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.DeliveryOperatorResponse;
import com.ms.mal_back.entity.enums.DeliveryStatus;
import com.ms.mal_back.service.DeliveryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/operator/deliveries")
@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
@RequiredArgsConstructor
public class DeliveryOperatorController {

    private final DeliveryService deliveryService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<DeliveryOperatorResponse>> getAllDeliveries(@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        return ResponseEntity.ok(deliveryService.getDeliveriesForOperator());
    }

    @PostMapping("/update-status")
    public ResponseEntity<Void> updateStatus(
            @RequestParam("deliveryId") Long deliveryId,
            @RequestParam("status") String statusString,
            @RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        DeliveryStatus status;
        try {
            status = DeliveryStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + statusString);
        }

        deliveryService.operatorUpdateStatus(deliveryId, status);
        return ResponseEntity.ok().build();
    }
}
