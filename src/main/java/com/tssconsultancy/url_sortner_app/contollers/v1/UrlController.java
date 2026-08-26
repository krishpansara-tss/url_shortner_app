package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.CustomUrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.enums.PaymentType;
import com.tssconsultancy.url_sortner_app.security.UserPrincipal;
import com.tssconsultancy.url_sortner_app.services.implementation.PaymentServiceImpl;
import com.tssconsultancy.url_sortner_app.services.implementation.UrlServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {
    private final UrlServiceImp urlService;
    private final PaymentServiceImpl paymentService;

    @PostMapping
    public ResponseEntity<UrlResponseDto> generateShortUrl(@RequestBody @Valid UrlRequestDto dto,
                                                           @AuthenticationPrincipal UserPrincipal currentUser) {
        Long userId = currentUser.getId();
        UrlResponseDto response = urlService.createShortUrl(dto, userId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/redirect/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String longUrl = urlService.resolveShortUrlAndRecordVisit(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }

    @PostMapping("/custom")
    public ResponseEntity<PaymentResponseDto> createCustomUrl(
            @RequestBody @Valid CustomUrlRequestDto dto,
            @AuthenticationPrincipal UserPrincipal currentUser) {

        Long userId = currentUser.getId();

        UrlResponseDto reservedUrl = urlService.createCustomUrl(dto, userId);

        PaymentResponseDto paymentBill = paymentService.initiatePayment(userId, reservedUrl.getUrlId(), PaymentType.CUSTOM_ALIAS);

        return ResponseEntity.status(HttpStatus.CREATED).body(paymentBill);
    }

    @GetMapping
    public ResponseEntity<PageResponse<UrlResponseDto>> getUserUrls(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Long userId = currentUser.getId();
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<UrlResponseDto> urls = urlService.getAllUrlByUserId(userId, pageable);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlResponseDto> getUrlById(
            @PathVariable("id") Long urlId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Long userId = currentUser.getId();

        UrlResponseDto response = urlService.getUrlByIdAndUserId(urlId, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUrl(
            @PathVariable("id") Long urlId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Long userId = currentUser.getId();

        urlService.deleteUrl(urlId, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/active/{id}")
    public ResponseEntity<Void> activeUrl(
            @PathVariable("id") Long urlId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        Long userId = currentUser.getId();

        urlService.activeUrl(urlId, userId);
        return ResponseEntity.noContent().build();
    }

    // TODO: URL RENEW
    public ResponseEntity<PaymentResponseDto> getUrlStatus(
            @PathVariable("id") Long urlId,
            @AuthenticationPrincipal UserPrincipal currentUser
    ){
        Long userId = currentUser.getId();
        PaymentResponseDto response = urlService.urlRenew(urlId, userId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
