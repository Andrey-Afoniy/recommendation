package ru.star.bank.recommendation.controller;

import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final CacheManager cacheManager;
    private final BuildProperties buildProperties;

    public ManagementController(CacheManager cacheManager, Optional<BuildProperties> buildProperties) {
        this.cacheManager = cacheManager;
        this.buildProperties = buildProperties.orElse(null);
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<Void> clearCaches() {
        cacheManager.getCacheNames().stream()
                .forEach(name -> cacheManager.getCache(name).clear());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        Map<String, String> info = new HashMap<>();
        if (buildProperties != null) {
            info.put("name", buildProperties.getName());
            info.put("version", buildProperties.getVersion());
        } else {
            info.put("name", "recommendation-service");
            info.put("version", "unknown");
        }
        return ResponseEntity.ok(info);
    }
}