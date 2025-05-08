package com.ms.mal_back.controller;

import com.ms.mal_back.config.JwtService;
import com.ms.mal_back.entity.TariffEntry;
import com.ms.mal_back.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tariffs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class TariffAdminController {

    private final JwtService jwtService;
    private final TariffRepository tariffRepository;

    @GetMapping
    public ResponseEntity<List<TariffEntry>> getAllTariffs(
            @RequestHeader("Authorization") String token
    ) {
        jwtService.validateToken(token);
        return ResponseEntity.ok(tariffRepository.findAllByOrderByPriorityAscDurationDaysAsc());
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateTariffs(
            @RequestBody List<TariffEntry> updatedTariffs,
            @RequestHeader("Authorization") String token
    ) {
        jwtService.validateToken(token);
        tariffRepository.saveAll(updatedTariffs);
        return ResponseEntity.ok().build();
    }
}
