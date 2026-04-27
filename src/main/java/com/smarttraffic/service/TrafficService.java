package com.smarttraffic.service;

import com.smarttraffic.model.TrafficData;
import com.smarttraffic.repository.TrafficDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrafficService {

    private final TrafficDataRepository repo;

    public TrafficService(TrafficDataRepository repo) {
        this.repo = repo;
    }

    public List<TrafficData> getAll()                      { return repo.findAll(); }
    public Optional<TrafficData> getById(Long id)          { return repo.findById(id); }
    public TrafficData save(TrafficData t)                  { return repo.save(t); }
    public void delete(Long id)                            { repo.deleteById(id); }
    public boolean exists(Long id)                         { return repo.existsById(id); }
    public List<TrafficData> getByLevel(String level)      { return repo.findByCongestionLevel(level); }
    public List<TrafficData> search(String q)              { return repo.findByLocationContainingIgnoreCase(q); }
    public List<TrafficData> getTopByVehicles()            { return repo.findAllOrderByVehicleCountDesc(); }
    public Double avgVehicles()                            { return repo.findAverageVehicleCount(); }
    public Long totalVehicles()                            { return repo.findTotalVehicleCount(); }
}