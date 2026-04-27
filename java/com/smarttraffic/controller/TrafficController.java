package com.smarttraffic.controller;

import com.smarttraffic.dto.ApiResponse;
import com.smarttraffic.model.TrafficData;
import com.smarttraffic.service.TrafficService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {

    private final TrafficService svc;

    public TrafficController(TrafficService svc) { this.svc = svc; }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TrafficData>>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String level) {

        List<TrafficData> list;
        if (search != null && !search.isBlank()) list = svc.search(search);
        else if (level != null && !level.isBlank()) list = svc.getByLevel(level);
        else list = svc.getAll();

        return ResponseEntity.ok(new ApiResponse<>(200, "Success", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrafficData>> getById(@PathVariable Long id) {
        return svc.getById(id)
                .map(t -> ResponseEntity.ok(new ApiResponse<>(200, "Found", t)))
                .orElse(ResponseEntity.status(404)
                        .body(new ApiResponse<>(404, "Not found", null)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<TrafficData>> create(@RequestBody TrafficData t) {
        if (t.getLocation() == null || t.getLocation().isBlank())
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(400, "Location required", null));
        t.setRecordedAt(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Created", svc.save(t)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TrafficData>> update(
            @PathVariable Long id, @RequestBody Map<String, Object> body) {

        return svc.getById(id).map(t -> {
            if (body.containsKey("location"))     t.setLocation((String) body.get("location"));
            if (body.containsKey("vehicleCount")) t.setVehicleCount((Integer) body.get("vehicleCount"));
            if (body.containsKey("averageSpeed")) t.setAverageSpeed(Double.parseDouble(body.get("averageSpeed").toString()));
            return ResponseEntity.ok(new ApiResponse<>(200, "Updated", svc.save(t)));
        }).orElse(ResponseEntity.status(404).body(new ApiResponse<>(404, "Not found", null)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        if (!svc.exists(id))
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Not found", null));
        svc.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Deleted", null));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> stats() {
        List<TrafficData> all = svc.getAll();
        long high  = all.stream().filter(t -> "HIGH".equals(t.getCongestionLevel())).count();
        long low   = all.stream().filter(t -> "LOW".equals(t.getCongestionLevel())).count();
        Long total = svc.totalVehicles();
        Double avg = svc.avgVehicles();

        return ResponseEntity.ok(new ApiResponse<>(200, "Stats", Map.of(
                "totalLocations", all.size(),
                "highCongestion", high,
                "lowCongestion", low,
                "totalVehicles", total != null ? total : 0,
                "avgVehicles", avg != null ? Math.round(avg) : 0
        )));
    }
}