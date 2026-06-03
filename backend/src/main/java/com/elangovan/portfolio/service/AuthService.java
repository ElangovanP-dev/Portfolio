package com.elangovan.portfolio.service;

import com.elangovan.portfolio.dto.request.LoginRequest;
import com.elangovan.portfolio.dto.request.RegisterRequest;
import com.elangovan.portfolio.dto.response.AuthResponse;
import com.elangovan.portfolio.entity.User;
import com.elangovan.portfolio.exception.BadRequestException;
import com.elangovan.portfolio.repository.UserRepository;
import com.elangovan.portfolio.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Register a new admin user.
     */
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .expiresIn(jwtTokenProvider.getExpirationMs())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    /**
     * Authenticate user and return JWT.
     */
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        return AuthResponse.builder()
                .token(token)
                .expiresIn(jwtTokenProvider.getExpirationMs())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
