package com.tssconsultancy.url_sortner_app.contollers.v1;

import com.tssconsultancy.url_sortner_app.dtos.urls.UrlRequestDto;
import com.tssconsultancy.url_sortner_app.dtos.urls.UrlResponseDto;
import com.tssconsultancy.url_sortner_app.services.implementation.UrlServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {
    private final UrlServiceImp urlService;

    @PostMapping
    public ResponseEntity<UrlResponseDto> generateShortUrl(@RequestBody UrlRequestDto dto,
                                                           @RequestParam Long userId){
        UrlResponseDto response = urlService.createShortUrl(dto, userId);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode){
        String longUrl = urlService.resolveShortUrlAndRecordVisit(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }
}
