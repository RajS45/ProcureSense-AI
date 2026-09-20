package com.procuresense.service;

import com.procuresense.dto.AuthResponse;
import com.procuresense.dto.LoginRequest;
import com.procuresense.dto.UserDTO;
import com.procuresense.model.User;
import com.procuresense.model.UserRole;
import com.procuresense.repository.UserRepository;
import com.procuresense.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponse login(
            LoginRequest request) {

        if (request == null
                || request.getEmail() == null
                || request.getPassword() == null) {

            throw new RuntimeException(
                    "Email and password are required"
            );
        }

        Optional<User> userOpt
                = userRepository.findByEmail(
                        request.getEmail()
                );

        if (userOpt.isEmpty()) {

            log.warn(
                    "Login failed: user not found"
            );

            throw new RuntimeException(
                    "Invalid credentials"
            );
        }

        User user
                = userOpt.get();

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new RuntimeException(
                    "Invalid credentials"
            );
        }

        if (!Boolean.TRUE.equals(
                user.getActive())) {

            throw new RuntimeException(
                    "User account is inactive"
            );
        }

        String token
                = jwtTokenProvider.generateToken(
                        user.getId(),
                        user.getEmail(),
                        user.getRole().name()
                );

        UserDTO userDTO
                = new UserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFullName(),
                        user.getRole().name()
                );

        return new AuthResponse(
                token,
                userDTO
        );
    }

    public void createDemoUsers() {

        createUserIfMissing(
                "analyst@procuresense.com",
                "Procurement Analyst",
                UserRole.ANALYST
        );

        createUserIfMissing(
                "manager@procuresense.com",
                "Procurement Manager",
                UserRole.MANAGER
        );

        createUserIfMissing(
                "admin@procuresense.com",
                "Admin",
                UserRole.ADMIN
        );
    }

    private void createUserIfMissing(
            String email,
            String fullName,
            UserRole role) {

        if (userRepository
                .findByEmail(email)
                .isPresent()) {

            return;
        }

        User user
                = User.builder()
                        .email(email)
                        .fullName(fullName)
                        .passwordHash(
                                passwordEncoder.encode(
                                        "demo123"
                                )
                        )
                        .role(role)
                        .active(true)
                        .build();

        userRepository.save(user);

        log.info(
                "Created demo user: {}",
                email
        );
    }
}
