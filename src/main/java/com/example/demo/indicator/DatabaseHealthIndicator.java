package com.example.demo.indicator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {

        boolean databaseAvailable = true;

        if(databaseAvailable) {
            return Health.up().withDetail("database", "Database is available").build();
        } else {
            return Health.down().withDetail("database", "Database is not available").build();
        }
    }
    
}
