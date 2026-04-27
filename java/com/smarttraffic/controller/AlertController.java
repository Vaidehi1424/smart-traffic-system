package com.smarttraffic.controller;

import com.smarttraffic.dto.ApiResponse;
import com.smarttraffic.model.Alert;
import com.smarttraffic.service.AlertService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService svc;

    public AlertController(AlertService svc) { this.svc = svc; }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Alert>>> getAll(
            @RequestParam(required = false) String status) {
        List<Alert> list = (status != null && status.equals("ACTIVE"))
                ? svc.getActive() : svc.getAll();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Alert>> getById(@PathVariable Long id) {
        return svc.getById(id)
                .map(a -> ResponseEntity.ok(new ApiResponse<>(200, "Found", a)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(404, "Not found", null)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<Alert>> create(@RequestBody Alert alert) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Alert created", svc.save(alert)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Alert>> update(
            @PathVariable Long id, @RequestBody Alert updated) {
        return svc.getById(id).map(a -> {
            if (updated.getLocation() != null)    a.setLocation(updated.getLocation());
            if (updated.getAlertType() != null)   a.setAlertType(updated.getAlertType());
            if (updated.getDescription() != null) a.setDescription(updated.getDescription());
            if (updated.getStatus() != null)      a.setStatus(updated.getStatus());
            if (updated.getSeverity() != null)    a.setSeverity(updated.getSeverity());
            return ResponseEntity.ok(new ApiResponse<>(200, "Updated", svc.save(a)));
        }).orElse(ResponseEntity.status(404).body(new ApiResponse<>(404, "Not found", null)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<Alert>> resolve(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Resolved", svc.resolve(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        if (!svc.exists(id))
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Not found", null));
        svc.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Deleted", null));
    }
}