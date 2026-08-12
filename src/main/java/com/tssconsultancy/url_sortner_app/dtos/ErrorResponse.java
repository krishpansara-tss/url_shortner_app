package com.tssconsultancy.url_sortner_app.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String path;
    private int status;
    private String error;
    private String message;
}
