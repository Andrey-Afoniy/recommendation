package ru.star.bank.recommendation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.star.bank.recommendation.dto.RecommendationDto;
import ru.star.bank.recommendation.service.RecommendationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<Map<String, Object>> getRecommendations(@PathVariable("user_id") UUID userId) {
        List<RecommendationDto> recommendations = service.getRecommendations(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", userId.toString());
        response.put("recommendations", recommendations);
        return ResponseEntity.ok(response);
    }
}