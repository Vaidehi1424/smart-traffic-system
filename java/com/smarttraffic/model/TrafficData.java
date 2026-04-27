package com.smarttraffic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "traffic_data")
public class TrafficData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(name = "vehicle_count", nullable = false)
    private int vehicleCount;

    @Column(name = "congestion_level", nullable = false)
    private String congestionLevel; // HIGH or LOW

    @Column(name = "average_speed")
    private double averageSpeed; // km/h

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;

    @PrePersist
    protected void prePersist() {
        if (recordedAt == null) recordedAt = LocalDateTime.now();
        // Auto-calculate congestion
        if (vehicleCount > 60) congestionLevel = "HIGH";
        else congestionLevel = "LOW";
    }

    @PreUpdate
    protected void preUpdate() {
        if (vehicleCount > 60) congestionLevel = "HIGH";
        else congestionLevel = "LOW";
    }

    public TrafficData() {}

    // Getters & Setters
    public Long getId()                          { return id; }
    public String getLocation()                  { return location; }
    public void setLocation(String l)            { this.location = l; }
    public int getVehicleCount()                 { return vehicleCount; }
    public void setVehicleCount(int v)           { this.vehicleCount = v; }
    public String getCongestionLevel()           { return congestionLevel; }
    public void setCongestionLevel(String c)     { this.congestionLevel = c; }
    public double getAverageSpeed()              { return averageSpeed; }
    public void setAverageSpeed(double s)        { this.averageSpeed = s; }
    public Double getLatitude()                  { return latitude; }
    public void setLatitude(Double lat)          { this.latitude = lat; }
    public Double getLongitude()                 { return longitude; }
    public void setLongitude(Double lon)         { this.longitude = lon; }
    public LocalDateTime getRecordedAt()         { return recordedAt; }
    public void setRecordedAt(LocalDateTime t)   { this.recordedAt = t; }
}