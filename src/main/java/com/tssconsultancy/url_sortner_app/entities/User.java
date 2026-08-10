package com.tssconsultancy.url_sortner_app.entities;

import com.tssconsultancy.url_sortner_app.enums.UserStatus;
import com.tssconsultancy.url_sortner_app.enums.UserTypes;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String mobileNumber;

    @Column(nullable = false)
    private String hashedPassword;

    private String profilePicturePath;

    @Enumerated(EnumType.STRING)
    private UserTypes role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private Integer remainingUrlSlots;

    @Builder.Default
    private boolean isVerified = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Url> urls = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<VarificationToken> verificationTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isVarified() {
        return isVerified;
    }

    public void setVarified(boolean varified) {
        this.isVerified = varified;
    }
}

