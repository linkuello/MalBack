package com.ms.mal_back.dto;

import com.ms.mal_back.entity.TariffEntry;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdUpgradeEligibilityResponse {
    private String qrPhotoUrl;
    private List<TariffEntry> tariffs;
}
