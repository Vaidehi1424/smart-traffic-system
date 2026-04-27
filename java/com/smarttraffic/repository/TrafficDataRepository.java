package  com.smarttraffic.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smarttraffic.model.TrafficData;

import java.util.List;

public interface TrafficDataRepository extends JpaRepository<TrafficData, Long> {

    List<TrafficData> findByCongestionLevel(String level);

    List<TrafficData> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT t FROM TrafficData t ORDER BY t.vehicleCount DESC")
    List<TrafficData> findAllOrderByVehicleCountDesc();

    @Query("SELECT AVG(t.vehicleCount) FROM TrafficData t")
    Double findAverageVehicleCount();

    @Query("SELECT SUM(t.vehicleCount) FROM TrafficData t")
    Long findTotalVehicleCount();
}