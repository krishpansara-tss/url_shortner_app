package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.CustomUrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlUpdateRequestDto;
import com.tssconsultancy.url_sortner_app.enums.PaymentType;
import com.tssconsultancy.url_sortner_app.services.implementation.PaymentServiceImpl;
import com.tssconsultancy.url_sortner_app.services.implementation.UrlServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {
    private final UrlServiceImp urlService;
    private final PaymentServiceImpl paymentService;

    @PostMapping
    public ResponseEntity<UrlResponseDto> generateShortUrl(@RequestBody UrlRequestDto dto,
                                                           @RequestParam Long userId){
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
            @RequestBody CustomUrlRequestDto dto,
            @RequestParam Long userId) {

        UrlResponseDto reservedUrl = urlService.createCustomUrl(dto, userId);

        PaymentResponseDto paymentBill = paymentService.initiatePayment(userId, reservedUrl.getUrlId(), PaymentType.CUSTOM_ALIAS);

        return ResponseEntity.status(HttpStatus.CREATED).body(paymentBill);
    }

    @GetMapping
    public ResponseEntity<PageResponse<UrlResponseDto>> getUserUrls(
            @RequestParam(defaultValue = "5") Integer page,
            @RequestParam(defaultValue = "0") Integer size,
            @RequestParam Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<UrlResponseDto> urls = urlService.getAllUrlByUserId(userId, pageable);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UrlResponseDto> getUrlById(
            @PathVariable("id") Long urlId,
            @RequestParam Long userId) {

        UrlResponseDto response = urlService.getUrlByIdAndUserId(urlId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UrlResponseDto> updateUrl(
            @PathVariable("id") Long urlId,
            @RequestBody UrlUpdateRequestDto dto,
            @RequestParam Long userId) {

        UrlResponseDto response = urlService.updateUrl(urlId, userId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUrl(
            @PathVariable("id") Long urlId,
            @RequestParam Long userId) {

        urlService.deleteUrl(urlId, userId);
        return ResponseEntity.noContent().build();
    }


    // TODO: URL STATUS
    // TODO: URL STATS
    // TODO: URL RENEW
}
