package ua.laboratory.lab_spring_task.util.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class LogInMetric {
    private final Counter apiCallCounter;

    public LogInMetric(MeterRegistry meterRegistry) {
        this.apiCallCounter = Counter.builder("login.calls.total")
                .description("Total number of Login calls")
                .register(meterRegistry);
    }

    public void increment() {
        apiCallCounter.increment();
    }
}
