package com.tssconsultancy.url_sortner_app.repositories;

import com.tssconsultancy.url_sortner_app.entities.VarificationToken;
import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VarificationToken, Long> {
    Optional<VarificationToken> findFirstByUserAndTokenTypeAndUsedFalseOrderByCreatedAtDesc(User user, TokenType tokenType);
}
