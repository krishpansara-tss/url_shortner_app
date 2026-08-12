package com.tssconsultancy.url_sortner_app.repositories;

import com.tssconsultancy.url_sortner_app.entities.Url;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);
    Optional<Url> findByLongUrl(String longUrl);
    Optional<Url> findByUrlIdAndUserUserId(Long urlId, Long userId);

    Page<Url> findAllByUserUserId(Long userId, Pageable pageable);

    boolean existsByShortUrl(String shortUrl);
    boolean existsByLongUrl(String longUrl);

    long countByUser(User user);

    long countByUserAndUrlStatus(User user, UrlStatus urlStatus);

    @Query("SELECT COALESCE(SUM(u.totalVisits), 0) FROM Url u WHERE u.user = :user")
    long sumTotalVisitsByUser(@Param("user") User user);
}

