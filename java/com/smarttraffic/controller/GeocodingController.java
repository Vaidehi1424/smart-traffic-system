package com.smarttraffic.controller;

import com.smarttraffic.dto.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/geocode")
public class GeocodingController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> geocode(@RequestParam String q) {
        if (q == null || q.isBlank())
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, "Query required", null));
        try {
            String url = "https://nominatim.openstreetmap.org/search?format=json&limit=1&q="
                    + q.replace(" ", "+");
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "SmartTrafficMonitoringSystem/1.0");
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            Object result = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class).getBody();
            return ResponseEntity.ok(new ApiResponse<>(200, "OK", result));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500, "Geocoding failed", null));
        }
    }
}