package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.Exceptions.CustomException;
import com.chriskocabas.redditclone.dto.*;
import com.chriskocabas.redditclone.model.*;
import com.chriskocabas.redditclone.repository.IRefreshTokenRepository;
import com.chriskocabas.redditclone.repository.IUserRepository;
import com.chriskocabas.redditclone.repository.IVerificationTokenRepository;
import com.chriskocabas.redditclone.security.JwtService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor

public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IVerificationTokenRepository verificationTokenRepository;
    private final IRefreshTokenRepository refreshTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Run every 24 hours
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        refreshTokenRepository.findAll().stream()
                .filter(token -> token.getExpirationDate().isBefore(now))
                .forEach(token -> refreshTokenRepository.delete(token));
    }

    @Transactional
    public void register(RegisterRequest registerRequest){
        var user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .created(Instant.now())
                .role(Role.USER)
                .enabled(false)
                .build();
        userRepository.save(user);
        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please activate your account",user.getEmail(),
                "Thank you for signing up with our Reddit clone, " +
                "please click the link below to activate your account: " +
                "http://localhost:8080/api/v1/auth/accountVerification/" + token));
    }

    @Transactional
    public String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        Instant expiryDate = Instant.now().plus(Duration.ofDays(1));
        verificationToken.setExpiryDate(expiryDate);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    @Transactional
    public void verifyAccount(String token) {

        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(()-> new CustomException("Invalid Token"));
        enableUser(verificationTokenOptional.get());
        verificationTokenRepository.deleteVerificationTokenByToken(verificationTokenOptional.get().getToken());
    }

    @Transactional
    public void enableUser(VerificationToken verificationToken) {
        @NotBlank(message="Username is required") String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(()->new CustomException("User with username: "+username+" not found!"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public ResponseEntity<AuthenticationResponse> login(LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                    loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String authenticationToken = jwtService.generateToken(authentication);
            String refreshToken = refreshTokenService.generateRefreshToken(authentication.getName()).getToken();
//            return new AuthenticationResponse(authenticationToken, getCurrentUser().getUsername());
            AuthenticationResponse response = AuthenticationResponse.builder()
                    .authenticationToken(authenticationToken)
                    .refreshToken(refreshToken)
                    .expirationDate(Instant.now().plusMillis(jwtService.getJwtExpirationInMillis()))
                    .username(getCurrentUser().getUsername())
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error during login: {}", e.getMessage());
            AuthenticationResponse errorResponse = AuthenticationResponse.builder()
                    .username("Login failed. Please check your credentials and try again.")
                    .build();

            System.out.print(errorResponse);

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }


    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String newAuthToken = jwtService.generateTokenWithUserName(refreshTokenRequest.getUsername());
        String refreshToken = refreshTokenService.generateRefreshToken(refreshTokenRequest.getUsername()).getToken();
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return AuthenticationResponse.builder()
                .authenticationToken(newAuthToken)
                .refreshToken(refreshToken)
                .expirationDate(Instant.now().plusMillis(jwtService.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName();

        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElse(null);
    }
}
