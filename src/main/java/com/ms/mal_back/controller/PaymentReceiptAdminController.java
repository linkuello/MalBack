package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.PaymentReceiptOperatorResponse;
import com.ms.mal_back.service.PaymentReceiptService;
import com.ms.mal_back.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operator/receipts")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
public class PaymentReceiptAdminController {

    private final PaymentReceiptService receiptService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<PaymentReceiptOperatorResponse>> getAll(@RequestHeader("Authorization") String token
    ) {
        jwtService.validateToken(token);
        return ResponseEntity.ok(receiptService.getAllReceiptsForOperator());
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirm(@RequestParam Long receiptId,@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        receiptService.confirmReceipt(receiptId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> reject(@RequestParam Long receiptId,@RequestHeader("Authorization") String token) {
        jwtService.validateToken(token);
        receiptService.rejectReceipt(receiptId);
        return ResponseEntity.ok().build();
    }
}
