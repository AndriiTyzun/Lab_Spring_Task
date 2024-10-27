package ua.laboratory.lab_spring_task.util.health_indicators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ApiHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        try {
            int responseCode = checkApiHealth();
            if (responseCode == 200) {
                return Health.up().withDetail("External API", "Available").build();
            }
            return Health.down().withDetail("External API", "Unavailable").build();
        } catch (Exception e) {
            return Health.down(e).withDetail("External API", "Connection failed").build();
        }
    }

    private int checkApiHealth() throws IOException {
        HttpURLConnection connection =
                (HttpURLConnection) new URL("http://localhost:8080/api/health/check").openConnection();
        connection.setRequestMethod("GET");
        return connection.getResponseCode();
    }
}
