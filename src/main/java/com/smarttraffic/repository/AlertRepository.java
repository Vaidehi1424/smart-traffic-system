package  com.smarttraffic.repository;

import com.smarttraffic.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByStatus(String status);
    List<Alert> findByAlertType(String alertType);
    List<Alert> findByLocationContainingIgnoreCase(String location);
    long countByStatus(String status);
}