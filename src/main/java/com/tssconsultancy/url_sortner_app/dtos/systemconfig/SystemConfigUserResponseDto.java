package com.tssconsultancy.url_sortner_app.dtos.systemconfig;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class SystemConfigUserResponseDto {
    private Integer maxVisitsPerFreeUrl;
    private Double renewalFee;
    private Integer renewalVisitsGranted;
    private Integer freeUrlQuotaPerUser;
    private Double pricePerAdditionalSlot;
}
