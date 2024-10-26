package ua.laboratory.lab_spring_task.util.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RegistrationMetric {
    private final Counter apiCallCounter;

    public RegistrationMetric(MeterRegistry meterRegistry) {
        this.apiCallCounter = Counter.builder("registration.calls.total")
                .description("Total number of Registration calls for both Trainers and Trainees")
                .register(meterRegistry);
    }

    public void increment() {
        apiCallCounter.increment();
    }
}
