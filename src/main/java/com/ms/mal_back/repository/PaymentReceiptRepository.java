package com.ms.mal_back.repository;

import com.ms.mal_back.entity.PaymentReceipt;
import com.ms.mal_back.entity.TariffEntry;
import com.ms.mal_back.entity.enums.Priority;
import com.ms.mal_back.entity.enums.ReceiptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Long> {
}
