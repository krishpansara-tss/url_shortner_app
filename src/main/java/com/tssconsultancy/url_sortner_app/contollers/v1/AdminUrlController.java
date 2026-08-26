package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import com.tssconsultancy.url_sortner_app.services.interfaces.IAdminUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/admin/urls")
@RequiredArgsConstructor
public class AdminUrlController {

    private final IAdminUrlService adminUrlService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UrlResponseDto>> getAllUrls(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponse<UrlResponseDto> urls = adminUrlService.getAllUrls(pageable);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UrlResponseDto> getUrlById(@PathVariable("id") Long urlId) {
        UrlResponseDto response = adminUrlService.getUrlById(urlId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UrlResponseDto> updateUrlStatus(
            @PathVariable("id") Long urlId,
            @RequestParam UrlStatus status) {

        UrlResponseDto response = adminUrlService.updateUrlStatus(urlId, status);
        return ResponseEntity.ok(response);
    }
}
