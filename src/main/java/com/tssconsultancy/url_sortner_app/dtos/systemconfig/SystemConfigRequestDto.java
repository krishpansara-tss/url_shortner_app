package com.tssconsultancy.url_sortner_app.dtos.systemconfig;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SystemConfigRequestDto {
    @Min(value = 0, message = "Max visits per free URL cannot be negative")
    private Integer maxVisitsPerFreeUrl;

    @PositiveOrZero(message = "Renewal fee must be zero or positive")
    private Double renewalFee;

    @Min(value = 1, message = "Renewal visits granted must be at least 1")
    private Integer renewalVisitsGranted;

    @Min(value = 0, message = "Free URL quota per user cannot be negative")
    private Integer freeUrlQuotaPerUser;

    @PositiveOrZero(message = "Price per additional slot must be zero or positive")
    private Double pricePerAdditionalSlot;
}
