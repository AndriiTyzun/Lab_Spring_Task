package ua.laboratory.lab_spring_task.service.implementation;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_TIME_DURATION = 5;

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> lockCache = new ConcurrentHashMap<>();

    public void recordFailedAttempt(String username) {
        int attempts = attemptsCache.getOrDefault(username, 0);
        attempts++;
        attemptsCache.put(username, attempts);

        if (attempts >= MAX_ATTEMPTS) {
            lockCache.put(username, LocalDateTime.now().plusMinutes(LOCK_TIME_DURATION));
        }
    }

    public boolean isBlocked(String username) {
        LocalDateTime lockTime = lockCache.get(username);
        if (lockTime == null) {
            return false;
        }
        if (lockTime.isBefore(LocalDateTime.now())) {
            attemptsCache.remove(username);
            lockCache.remove(username);
            return false;
        }
        return true;
    }

    public void resetAttempts(String username) {
        attemptsCache.remove(username);
        lockCache.remove(username);
    }
}
