package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.dto.AdUpgradeEligibilityResponse;
import com.ms.mal_back.dto.UpgradeReceiptRequest;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.PaymentReceipt;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.mapper.PaymentReceiptMapper;
import com.ms.mal_back.repository.AdvertisementRepository;
import com.ms.mal_back.repository.UserRepository;
import com.ms.mal_back.service.PaymentReceiptService;
import com.ms.mal_back.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class PaymentReceiptController {

    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final AdvertisementRepository adRepo;
    private final PhotoService photoService;
    private final PaymentReceiptService receiptService;

    @GetMapping("/check-upgrade-eligibility")
    public ResponseEntity<AdUpgradeEligibilityResponse> checkEligibility(
            @RequestHeader("Authorization") String token,
            @RequestParam("adId") Long adId) {

        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        AdUpgradeEligibilityResponse response = receiptService.checkAdUpgradeEligibility(userId, adId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/upload-receipt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadReceipt(
            @RequestHeader("Authorization") String token,
            @RequestPart("form") UpgradeReceiptRequest form,
            @RequestPart("photo") MultipartFile photo) {

        jwtService.validateToken(token);
        Long userId = jwtService.extractUserId(token);
        User user = userRepo.findById(userId).orElseThrow();
        Advertisement ad = adRepo.findById(form.getAdId()).orElseThrow();

        PaymentReceipt receipt = receiptService.createReceipt(user, ad, form);
        photoService.saveReceiptPhoto(receipt, photo);

        return ResponseEntity.ok()
                .header("X-Message", "Ваш чек загружен. Ожидайте подтверждение от оператора.")
                .build();
    }

}
