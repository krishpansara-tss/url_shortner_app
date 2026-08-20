package com.tssconsultancy.url_sortner_app.dtos.systemconfig;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SystemConfigResponseDto {
    private Integer maxVisitsPerFreeUrl;
    private Double renewalFee;
    private Integer renewalVisitsGranted;
    private Integer freeUrlQuotaPerUser;
    private Double pricePerAdditionalSlot;
//    private LocalDateTime updatedAt;
}
