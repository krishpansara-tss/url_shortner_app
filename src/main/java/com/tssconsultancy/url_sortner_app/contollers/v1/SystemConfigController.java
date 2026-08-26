package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.systemconfig.SystemConfigResponseDto;
import com.tssconsultancy.url_sortner_app.services.interfaces.ISystemConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/admin/system-config")
@RequiredArgsConstructor
public class SystemConfigController {

    private final ISystemConfigService systemConfigService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfigResponseDto> getSystemConfiguration() {
        SystemConfigResponseDto response = systemConfigService.getSystemConfiguration();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemConfigResponseDto> updateSystemConfiguration(@RequestBody @Valid SystemConfigRequestDto dto) {
        SystemConfigResponseDto response = systemConfigService.updateSystemConfiguration(dto);
        return ResponseEntity.ok(response);
    }
}
