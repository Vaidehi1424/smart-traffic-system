package com.smarttraffic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String alertType; // ACCIDENT, CONGESTION, ROAD_CLOSURE, FLOOD

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private String status; // ACTIVE, RESOLVED

    @Column(name = "severity")
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null)    status = "ACTIVE";
        if (severity == null)  severity = "MEDIUM";
    }

    public Alert() {}

    public Alert(String location, String alertType, String description,
                 String status, Double lat, Double lon) {
        this.location    = location;
        this.alertType   = alertType;
        this.description = description;
        this.status      = status;
        this.latitude    = lat;
        this.longitude   = lon;
        this.createdAt   = LocalDateTime.now();
        this.severity    = "MEDIUM";
    }

    // Getters & Setters
    public Long getId()                          { return id; }
    public String getLocation()                  { return location; }
    public void setLocation(String l)            { this.location = l; }
    public String getAlertType()                 { return alertType; }
    public void setAlertType(String t)           { this.alertType = t; }
    public String getDescription()               { return description; }
    public void setDescription(String d)         { this.description = d; }
    public String getStatus()                    { return status; }
    public void setStatus(String s)              { this.status = s; }
    public String getSeverity()                  { return severity; }
    public void setSeverity(String s)            { this.severity = s; }
    public Double getLatitude()                  { return latitude; }
    public void setLatitude(Double lat)          { this.latitude = lat; }
    public Double getLongitude()                 { return longitude; }
    public void setLongitude(Double lon)         { this.longitude = lon; }
    public LocalDateTime getCreatedAt()          { return createdAt; }
    public void setCreatedAt(LocalDateTime t)    { this.createdAt = t; }
    public LocalDateTime getResolvedAt()         { return resolvedAt; }
    public void setResolvedAt(LocalDateTime t)   { this.resolvedAt = t; }
}