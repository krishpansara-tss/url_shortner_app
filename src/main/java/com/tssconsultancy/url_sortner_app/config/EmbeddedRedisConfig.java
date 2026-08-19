package com.tssconsultancy.url_sortner_app.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import java.io.IOException;

@Configuration
@Slf4j
public class EmbeddedRedisConfig {
    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = RedisServer.newRedisServer()
                .port(6379)
                .setting("maxmemory 128M")
                .build();

        redisServer.start();
        log.info("[EMBEDDED REDIS] Embedded Redis server started on Port : {}", redisServer.ports());
    }

    @PreDestroy
    public void stopRedis() throws IOException {
        if (redisServer != null) {
            redisServer.stop();
            log.info("[EMBEDDED REDIS] Embedded Redis server stopped");
        }
    }
}
