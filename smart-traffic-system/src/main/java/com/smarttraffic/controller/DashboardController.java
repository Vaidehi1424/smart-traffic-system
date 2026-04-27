package com.smarttraffic.controller;

import com.smarttraffic.dto.ApiResponse;
import com.smarttraffic.model.Alert;
import com.smarttraffic.model.TrafficData;
import com.smarttraffic.service.AlertService;
import com.smarttraffic.service.TrafficService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final TrafficService trafficSvc;
    private final AlertService alertSvc;

    public DashboardController(TrafficService trafficSvc, AlertService alertSvc) {
        this.trafficSvc = trafficSvc;
        this.alertSvc = alertSvc;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        try {

            List<TrafficData> traffic = Optional.ofNullable(trafficSvc.getAll()).orElse(new ArrayList<>());
            List<Alert> alerts = Optional.ofNullable(alertSvc.getAll()).orElse(new ArrayList<>());

            long highCount = traffic.stream()
                    .filter(t -> "HIGH".equalsIgnoreCase(t.getCongestionLevel()))
                    .count();

            long lowCount = traffic.stream()
                    .filter(t -> "LOW".equalsIgnoreCase(t.getCongestionLevel()))
                    .count();

            Long totalVeh = 0L;
            Double avgVeh = 0.0;

            try {
                totalVeh = Optional.ofNullable(trafficSvc.totalVehicles()).orElse(0L);
            } catch (Exception e) {
                System.out.println("totalVehicles error: " + e.getMessage());
            }

            try {
                avgVeh = Optional.ofNullable(trafficSvc.avgVehicles()).orElse(0.0);
            } catch (Exception e) {
                System.out.println("avgVehicles error: " + e.getMessage());
            }

            long activeAlerts = 0;
            try {
                activeAlerts = alertSvc.countActive();
            } catch (Exception e) {
                System.out.println("countActive error: " + e.getMessage());
            }

            Optional<TrafficData> busiest = traffic.stream()
                    .max(Comparator.comparingInt(TrafficData::getVehicleCount));

            List<Map<String, Object>> chartData = traffic.stream()
                    .sorted(Comparator.comparingInt(TrafficData::getVehicleCount).reversed())
                    .limit(8)
                    .map(t -> Map.<String, Object>of(
                            "location", t.getLocation(),
                            "vehicles", t.getVehicleCount(),
                            "level", t.getCongestionLevel()
                    ))
                    .collect(Collectors.toList());

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalLocations", traffic.size());
            stats.put("highCongestion", highCount);
            stats.put("lowCongestion", lowCount);
            stats.put("totalVehicles", totalVeh);
            stats.put("avgVehicles", Math.round(avgVeh));
            stats.put("activeAlerts", activeAlerts);
            stats.put("totalAlerts", alerts.size());
            stats.put("busiestLocation", busiest.map(TrafficData::getLocation).orElse("N/A"));
            stats.put("busiestVehicles", busiest.map(TrafficData::getVehicleCount).orElse(0));
            stats.put("chartData", chartData);

            return ResponseEntity.ok(new ApiResponse<>(200, "Dashboard stats", stats));

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 IMPORTANT
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500, "Server error", null));
        }
    }
}