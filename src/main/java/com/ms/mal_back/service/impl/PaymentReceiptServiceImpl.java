package com.ms.mal_back.service.impl;

import com.ms.mal_back.dto.AdUpgradeEligibilityResponse;
import com.ms.mal_back.dto.PaymentReceiptOperatorResponse;
import com.ms.mal_back.dto.UpgradeReceiptRequest;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.PaymentReceipt;
import com.ms.mal_back.entity.TariffEntry;
import com.ms.mal_back.entity.User;
import com.ms.mal_back.entity.enums.Priority;
import com.ms.mal_back.entity.enums.ReceiptStatus;
import com.ms.mal_back.exception.ConflictException;
import com.ms.mal_back.exception.ForbiddenAccessException;
import com.ms.mal_back.mapper.PaymentReceiptMapper;
import com.ms.mal_back.repository.AdvertisementRepository;
import com.ms.mal_back.repository.PaymentReceiptRepository;
import com.ms.mal_back.repository.TariffRepository;
import com.ms.mal_back.service.PaymentReceiptService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentReceiptServiceImpl implements PaymentReceiptService {

    private final AdvertisementRepository adRepository;
    private final PaymentReceiptRepository receiptRepository;
    private final TariffRepository tariffRepository;
    private final PaymentReceiptMapper receiptMapper;

    @Override
    public AdUpgradeEligibilityResponse checkAdUpgradeEligibility(Long userId, Long adId) {
        Advertisement ad = adRepository.findById(adId)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));

        if (!ad.getSeller().getId().equals(userId)) {
            throw new ForbiddenAccessException("You do not own this advertisement.");
        }

        if (ad.getPriority() != Priority.STANDARD) {
            throw new ConflictException("This advertisement is already upgraded.");
        }

        List<TariffEntry> tariffs = tariffRepository.findAll();

        AdUpgradeEligibilityResponse response = new AdUpgradeEligibilityResponse();
        response.setQrPhotoUrl("/static/qr/upgrade.png"); // or from config
        response.setTariffs(tariffs);
        return response;
    }

    @Override
    public PaymentReceipt createReceipt(User user, Advertisement ad, UpgradeReceiptRequest dto) {
        Priority requestedPriority = Priority.valueOf(dto.getRequestedPriority().toUpperCase());
        int durationDays = Math.toIntExact(dto.getDurationMillis() / (1000 * 60 * 60 * 24));

        tariffRepository.findByPriorityAndDurationDays(requestedPriority, durationDays)
                .orElseThrow(() -> new IllegalArgumentException("Invalid priority or duration"));

        PaymentReceipt receipt = new PaymentReceipt();
        receipt.setUser(user);
        receipt.setAdvertisement(ad);
        receipt.setRequestedPriority(requestedPriority);
        receipt.setDurationDays(durationDays);
        receipt.setSubmittedAt(LocalDateTime.now());
        receipt.setStatus(ReceiptStatus.NEW);

        return receiptRepository.save(receipt);
    }

    @Override
    public List<PaymentReceiptOperatorResponse> getAllReceiptsForOperator() {
        List<PaymentReceipt> receipts = receiptRepository.findAll();
        return receiptMapper.toOperatorResponses(receipts);
    }

    @Override
    public void confirmReceipt(Long receiptId) {
        PaymentReceipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new EntityNotFoundException("Receipt not found"));

        if (receipt.getStatus() != ReceiptStatus.NEW) {
            throw new ConflictException("Receipt already processed");
        }

        Advertisement ad = receipt.getAdvertisement();
        ad.setPriority(receipt.getRequestedPriority());
        ad.setPriorityUntil(LocalDate.now().plusDays(receipt.getDurationDays()));

        receipt.setStatus(ReceiptStatus.CONFIRMED);

        adRepository.save(ad);
        receiptRepository.save(receipt);
    }

    @Override
    public void rejectReceipt(Long receiptId) {
        PaymentReceipt receipt = receiptRepository.findById(receiptId)
                .orElseThrow(() -> new EntityNotFoundException("Receipt not found"));

        if (receipt.getStatus() != ReceiptStatus.NEW) {
            throw new ConflictException("Receipt already processed");
        }

        receipt.setStatus(ReceiptStatus.FRAUDULENT);
        receiptRepository.save(receipt);
    }
}

