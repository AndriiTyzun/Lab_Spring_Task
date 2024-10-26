package ua.laboratory.lab_spring_task.util.health_indicators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBHealthIndicator implements HealthIndicator {
    private final DataSource dataSource;

    public DBHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1000)) {
                return Health.up().withDetail("Database", "Connection is OK").build();
            } else {
                return Health.down().withDetail("Database", "Connection is NOT valid").build();
            }
        } catch (SQLException e) {
            return Health.down(e).withDetail("Database", "Connection failed").build();
        }
    }
}
