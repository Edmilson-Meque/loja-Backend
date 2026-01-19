package com.ecommerce.ecommerce.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig {

    // Configurações de rate limiting por tipo de endpoint
    public enum BucketType {
        PUBLIC(20),      // 20 requests por minuto para endpoints públicos
        AUTHENTICATED(50), // 50 requests por minuto para usuários autenticados
        ADMIN(100),      // 100 requests por minuto para admin
        UPLOAD(10);      // 10 uploads por minuto

        private final int capacity;

        BucketType(int capacity) {
            this.capacity = capacity;
        }

        public int getCapacity() {
            return capacity;
        }
    }

    @Bean
    public Map<String, Bucket> buckets() {
        Map<String, Bucket> bucketMap = new ConcurrentHashMap<>();

        for (BucketType type : BucketType.values()) {
            Bandwidth limit = Bandwidth.classic(type.getCapacity(),
                    Refill.intervally(type.getCapacity(), Duration.ofMinutes(1)));
            bucketMap.put(type.name(), Bucket.builder().addLimit(limit).build());
        }

        return bucketMap;
    }
}