package com.tssconsultancy.url_sortner_app.repositories;

import com.tssconsultancy.url_sortner_app.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);
    boolean existsByShortUrl(String shortUrl);
    boolean existsByLongUrl(String longUrl);
    Optional<Url> findByLongUrl(String longUrl);
}

