package ua.laboratory.lab_spring_task.controller.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.dto.UserCredentials;
import ua.laboratory.lab_spring_task.service.implementation.LoginAttemptService;
import ua.laboratory.lab_spring_task.service.implementation.LogoutService;
import ua.laboratory.lab_spring_task.util.JwtUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper mapper;
    private final LoginAttemptService loginAttemptService;
    private final LogoutService logoutService;

    public SecurityFilter(JwtUtil jwtUtil, UserRepository userRepository, ObjectMapper mapper, LoginAttemptService loginAttemptService, LogoutService logoutService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.loginAttemptService = loginAttemptService;
        this.logoutService = logoutService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, Object> errorDetails = new HashMap<>();
        if ("/api/login".equals(request.getRequestURI())) {
            String username = request.getParameter("username");
            if (username != null && loginAttemptService.isBlocked(username)) {
                errorDetails.put("message", "User account is locked due to too many failed login attempts. Please try again later.");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(response.getWriter(), errorDetails);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
