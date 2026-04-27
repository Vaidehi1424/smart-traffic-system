package com.smarttraffic;

import com.smarttraffic.model.*;
import com.smarttraffic.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class SmartTrafficApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartTrafficApplication.class, args);
    }

    /**
     * Seeds default users and sample traffic data on first startup.
     */
    @Bean
    CommandLineRunner seedData(
            UserRepository userRepo,
            TrafficDataRepository trafficRepo,
            AlertRepository alertRepo,
            PasswordEncoder encoder) {

        return args -> {

            // ── Seed Users ──────────────────────────────────────
            if (userRepo.count() == 0) {
            	User admin = new User();
            	admin.setUsername("admin");
            	admin.setPassword(encoder.encode("admin123"));
            	admin.setRole(Role.ADMIN);
            	admin.setEmail("admin@smarttraffic.com");
            	
            	userRepo.save(admin);

            	User user1 = new User();
            	user1.setUsername("user");
            	user1.setPassword(encoder.encode("user123"));
            	user1.setRole(Role.USER);
            	user1.setEmail("user@smarttraffic.com");

            	User user2 = new User();
            	user2.setUsername("john");
            	user2.setPassword(encoder.encode("john123"));
            	user2.setRole(Role.USER);
            	user2.setEmail("john@smarttraffic.com");

            	
            	userRepo.save(user1);
            	userRepo.save(user2);
                System.out.println("✅ Users seeded: admin / user / john");
            }

            // ── Seed Traffic Data ───────────────────────────────
            if (trafficRepo.count() == 0) {
                String[][] locations = {
                    {"Hitech City",      "17.4486", "78.3908", "95",  "HIGH"},
                    {"Gachibowli",       "17.4401", "78.3489", "120", "HIGH"},
                    {"Madhapur",         "17.4504", "78.3803", "75",  "HIGH"},
                    {"KPHB Colony",      "17.4840", "78.3922", "40",  "LOW"},
                    {"Ameerpet",         "17.4374", "78.4487", "88",  "HIGH"},
                    {"Banjara Hills",    "17.4156", "78.4347", "35",  "LOW"},
                    {"Jubilee Hills",    "17.4321", "78.4071", "28",  "LOW"},
                    {"Secunderabad",     "17.4399", "78.4983", "110", "HIGH"},
                    {"Medchal",          "17.6277", "78.4812", "22",  "LOW"},
                    {"Kukatpally",       "17.4849", "78.4138", "65",  "HIGH"},
                    {"LB Nagar",         "17.3458", "78.5516", "55",  "HIGH"},
                    {"Dilsukhnagar",     "17.3688", "78.5247", "48",  "LOW"},
                    {"Charminar",        "17.3616", "78.4747", "80",  "HIGH"},
                    {"Abids",            "17.3850", "78.4867", "60",  "HIGH"},
                    {"Begumpet",         "17.4432", "78.4682", "45",  "LOW"},
                };
                for (String[] loc : locations) {
                    TrafficData td = new TrafficData();
                    td.setLocation(loc[0]);
                    td.setLatitude(Double.parseDouble(loc[1]));
                    td.setLongitude(Double.parseDouble(loc[2]));
                    td.setVehicleCount(Integer.parseInt(loc[3]));
                    td.setCongestionLevel(loc[4]);
                    td.setAverageSpeed(loc[4].equals("HIGH") ? 18.5 : 54.2);
                    td.setRecordedAt(LocalDateTime.now().minusMinutes((long)(Math.random() * 60)));
                    trafficRepo.save(td);
                }
                System.out.println("✅ Traffic data seeded: 15 locations");
            }

            // ── Seed Alerts ─────────────────────────────────────
            if (alertRepo.count() == 0) {
                alertRepo.save(new Alert("Hitech City",    "ACCIDENT",    "Multi-vehicle collision on ORR exit",    "ACTIVE",  17.4486, 78.3908));
                alertRepo.save(new Alert("Gachibowli",     "CONGESTION",  "Heavy traffic near DLF junction",        "ACTIVE",  17.4401, 78.3489));
                alertRepo.save(new Alert("Ameerpet",       "ROAD_CLOSURE","Road closed for metro construction",     "ACTIVE",  17.4374, 78.4487));
                alertRepo.save(new Alert("Secunderabad",   "ACCIDENT",    "Minor accident near Paradise circle",    "RESOLVED",17.4399, 78.4983));
                alertRepo.save(new Alert("Charminar",      "FLOOD",       "Waterlogging reported near underpass",   "ACTIVE",  17.3616, 78.4747));
                System.out.println("✅ Alerts seeded: 5 alerts");
            }
        };
    }
}