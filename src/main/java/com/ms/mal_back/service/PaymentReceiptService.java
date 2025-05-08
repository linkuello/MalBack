package com.ms.mal_back.service;

import com.ms.mal_back.dto.AdUpgradeEligibilityResponse;
import com.ms.mal_back.dto.PaymentReceiptOperatorResponse;
import com.ms.mal_back.dto.UpgradeReceiptRequest;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.PaymentReceipt;
import com.ms.mal_back.entity.User;

import java.util.List;

public interface PaymentReceiptService {
    public AdUpgradeEligibilityResponse checkAdUpgradeEligibility(Long userId, Long adId);
    public PaymentReceipt createReceipt(User user, Advertisement ad, UpgradeReceiptRequest dto);
    public List<PaymentReceiptOperatorResponse> getAllReceiptsForOperator();
    public void confirmReceipt(Long receiptId) ;
    public void rejectReceipt(Long receiptId);
}
