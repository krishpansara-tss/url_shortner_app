package com.tssconsultancy.url_sortner_app.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUsageResponseDto {
    private Long userId;
    private String email;
    private Integer remainingUrlSlots;
    private long totalUrlsCreated;
    private long totalUrlVisits;
    private long activeUrlsCount;
}
