package com.ms.mal_back.mapper.impl;

import com.ms.mal_back.config.UrlBuilder;
import com.ms.mal_back.dto.PaymentReceiptOperatorResponse;
import com.ms.mal_back.dto.UpgradeReceiptRequest;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.PaymentReceipt;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.entity.enums.Priority;
import com.ms.mal_back.entity.enums.ReceiptStatus;
import com.ms.mal_back.mapper.PaymentReceiptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentReceiptMapperImpl implements PaymentReceiptMapper {

    private final UrlBuilder urlBuilder;

    @Override
    public PaymentReceipt toEntity(User user, Advertisement ad, UpgradeReceiptRequest dto) {
        PaymentReceipt receipt = new PaymentReceipt();
        receipt.setUser(user);
        receipt.setAdvertisement(ad);
        receipt.setRequestedPriority(Priority.valueOf(dto.getRequestedPriority().toUpperCase()));
        receipt.setDurationDays(Math.toIntExact(dto.getDurationMillis() / (1000 * 60 * 60 * 24)));
        receipt.setSubmittedAt(LocalDateTime.now());
        receipt.setStatus(ReceiptStatus.NEW);
        return receipt;
    }

    @Override
    public PaymentReceiptOperatorResponse toOperatorResponse(PaymentReceipt receipt) {
        return new PaymentReceiptOperatorResponse() {{
            setReceiptId(receipt.getId());
            setUsername(receipt.getUser().getUsername());
            setUserPhone(receipt.getUser().getPhone());
            setAdId(receipt.getAdvertisement().getId());
            setAdTitle(receipt.getAdvertisement().getBreed() + " (" + receipt.getAdvertisement().getAnimal() + ")");
            setRequestedPriority(receipt.getRequestedPriority());
            setDurationDays(receipt.getDurationDays());
            setStatus(receipt.getStatus());
            setSubmittedAt(receipt.getSubmittedAt());

            // ✅ Fixed: use URL builder instead of file path
            String photoUrl = receipt.getReceiptPhoto() != null
                    ? urlBuilder.buildFullPhotoUrl(receipt.getReceiptPhoto().getId())
                    : null;
            setReceiptPhotoUrl(photoUrl);
        }};
    }

    @Override
    public List<PaymentReceiptOperatorResponse> toOperatorResponses(List<PaymentReceipt> receipts) {
        return receipts.stream()
                .map(this::toOperatorResponse)
                .toList();
    }
}