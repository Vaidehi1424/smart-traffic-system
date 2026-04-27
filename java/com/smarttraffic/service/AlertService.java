package com.smarttraffic.service;

import com.smarttraffic.model.Alert;
import com.smarttraffic.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    private final AlertRepository repo;

    public AlertService(AlertRepository repo) {
        this.repo = repo;
    }

    public List<Alert> getAll()                  { return repo.findAll(); }
    public List<Alert> getActive()               { return repo.findByStatus("ACTIVE"); }
    public Optional<Alert> getById(Long id)      { return repo.findById(id); }
    public Alert save(Alert a)                   { return repo.save(a); }
    public void delete(Long id)                  { repo.deleteById(id); }
    public boolean exists(Long id)               { return repo.existsById(id); }
    public long countActive()                    { return repo.countByStatus("ACTIVE"); }

    public Alert resolve(Long id) {
        Alert a = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found: " + id));
        a.setStatus("RESOLVED");
        a.setResolvedAt(LocalDateTime.now());
        return repo.save(a);
    }
}