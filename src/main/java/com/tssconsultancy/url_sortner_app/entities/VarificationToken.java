package com.tssconsultancy.url_sortner_app.entities;

import com.tssconsultancy.url_sortner_app.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class VarificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @NonNull
    @Column(name = "hashed_token", nullable = false, unique = true)
    private String hashedToken;

    private boolean used = false;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime usedAt;

    @NonNull
    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

