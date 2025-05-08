package com.ms.mal_back.mapper;

import com.ms.mal_back.dto.PaymentReceiptOperatorResponse;
import com.ms.mal_back.dto.UpgradeReceiptRequest;
import com.ms.mal_back.entity.Advertisement;
import com.ms.mal_back.entity.PaymentReceipt;
import com.ms.mal_back.entity.User;

import java.util.List;

public interface PaymentReceiptMapper {
    public PaymentReceipt toEntity(User user, Advertisement ad, UpgradeReceiptRequest dto) ;
    public PaymentReceiptOperatorResponse toOperatorResponse(PaymentReceipt receipt) ;
    public List<PaymentReceiptOperatorResponse> toOperatorResponses(List<PaymentReceipt> receipts);
}