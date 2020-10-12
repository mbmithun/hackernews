package com.insider.hackernews.config;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CaffeineConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(CaffeineConfiguration.class);
	@Bean
    public Caffeine<Object, Object> caffeineConfig() {
		logger.info("Initializing cache config");
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats();
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("story", "comment", "user");
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
