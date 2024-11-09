package ua.laboratory.lab_spring_task.service.implementation;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.dto.UserCredentials;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    public UserService(UserRepository userRepository, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked(username)) {
            throw new RuntimeException("Blocked due to too many failed login attempts");
        }
        User user = userRepository.getByUsername(username).orElseThrow();
        return new UserCredentials(user.getUsername(), user.getPassword());
    }
}
