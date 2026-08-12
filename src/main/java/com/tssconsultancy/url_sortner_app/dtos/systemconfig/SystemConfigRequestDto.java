package com.tssconsultancy.url_sortner_app.dtos.systemconfig;

import lombok.*;

@Data
@RequiredArgsConstructor
public class SystemConfigRequestDto {
    private Integer maxVisitsPerFreeUrl;
    private Double renewalFee;
    private Integer renewalVisitsGranted;
    private Integer freeUrlQuotaPerUser;
    private Double pricePerAdditionalSlot;
}
