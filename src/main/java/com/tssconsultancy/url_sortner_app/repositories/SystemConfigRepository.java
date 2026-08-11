package com.tssconsultancy.url_sortner_app.repositories;

import com.tssconsultancy.url_sortner_app.entities.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
    Optional<SystemConfig> findByConfigKey(String configKey);
}

