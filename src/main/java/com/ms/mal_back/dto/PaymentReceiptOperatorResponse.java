package com.ms.mal_back.dto;

import com.ms.mal_back.entity.enums.Priority;
import com.ms.mal_back.entity.enums.ReceiptStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentReceiptOperatorResponse {
    private Long receiptId;
    private String username;
    private String userPhone;
    private Long adId;
    private String adTitle;
    private Priority requestedPriority;
    private int durationDays;
    private String receiptPhotoUrl;
    private ReceiptStatus status;
    private LocalDateTime submittedAt;
}

