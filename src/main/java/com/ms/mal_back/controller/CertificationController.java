//package com.ms.mal_back.controller;
//
//import com.ms.mal_back.config.JwtService;
//import com.ms.mal_back.dto.CertificationRequest;
//import com.ms.mal_back.dto.CertificationResponse;
//import com.ms.mal_back.service.CertificationService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//
//@Slf4j
//@RestController
//@RequestMapping("/api/certifications")
//@RequiredArgsConstructor
//public class CertificationController {
//
//    private final CertificationService certificationService;
//    private final JwtService jwtService;
//
//    @GetMapping("/by-user/{userId}")
//    public ResponseEntity<List<CertificationResponse>> getCertificationsByUser(@PathVariable Long userId) {
//        log.info("Fetching certifications for user {}", userId);
//        return ResponseEntity.ok(certificationService.getCertificationsByUser(userId));
//    }
//    @GetMapping("/my")
//    public ResponseEntity<List<CertificationResponse>> getOwnCertifications(@RequestHeader("Authorization") String token) {
//        jwtService.validateToken(token);
//        Long userId = jwtService.extractUserId(token);
//        log.info("Fetching own certifications for user {}", userId);
//        return ResponseEntity.ok(certificationService.getCertificationsByUser(userId));
//    }
//    @PostMapping("/create")
//    public ResponseEntity<CertificationResponse> createCertification(
//            @RequestHeader("Authorization") String token,
//            @RequestPart("cert") CertificationRequest request,
//            @RequestPart("photo") MultipartFile file) {
//
//        jwtService.validateToken(token);
//        Long userId = jwtService.extractUserId(token);
//        log.info("Creating certification for user {}", userId);
//        CertificationResponse response = certificationService.createCertification(userId, request, file);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//    @DeleteMapping("/{certificationId}")
//    public ResponseEntity<Void> deleteCertification(
//            @RequestHeader("Authorization") String token,
//            @PathVariable Long certificationId) {
//
//        jwtService.validateToken(token);
//        Long userId = jwtService.extractUserId(token);
//        log.info("Deleting certification {} for user {}", certificationId, userId);
//        certificationService.deleteCertification(certificationId, userId);
//        return ResponseEntity.noContent().build();
//    }
//}
