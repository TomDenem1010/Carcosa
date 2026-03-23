package com.home.carcosa.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "boardgame.findAll.dto",
                "boardgame.findAll.entity",
                "boardgame.findAll.entity.sorted",
                "boardgame.findByName",
                "boardgameGroup.findAll.dto",
                "boardgameGroup.findAll.entity.sorted",
                "boardgamePlay.findAll.dto",
                "boardgamePlay.findAll.entity.sorted",
                "boardgamePlay.findByBoardgameName");
    }
}
