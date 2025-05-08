package com.ms.mal_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpgradeReceiptRequest {
    private Long adId;
    private String requestedPriority; // "FEATURED" or "PREMIUM"
    private Long durationMillis;      // 15d, 1mo, etc. sent in ms
}

