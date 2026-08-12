package com.tssconsultancy.url_sortner_app.repositories;

import com.tssconsultancy.url_sortner_app.entities.Url;
import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UrlRepository extends JpaRepository<Url, Long> {
    long countByUser(User user);

    long countByUserAndUrlStatus(User user, UrlStatus urlStatus);

    @Query("SELECT COALESCE(SUM(u.totalVisits), 0) FROM Url u WHERE u.user = :user")
    long sumTotalVisitsByUser(@Param("user") User user);
}

