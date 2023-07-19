package com.chriskocabas.redditclone.service;

import com.chriskocabas.redditclone.dto.*;
import com.chriskocabas.redditclone.model.NotificationEmail;
import com.chriskocabas.redditclone.model.User;
import com.chriskocabas.redditclone.model.VerificationToken;
import com.chriskocabas.redditclone.repository.IUserRepository;
import com.chriskocabas.redditclone.repository.IVerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IVerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;


    @Transactional
    public void signUp(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please activate your account",user.getEmail(),
                "Thank you for signing up with our Reddit clone, " +
                "please click the link below to activate your account: " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }


    public void verifyAccount(String token) {
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        //TODO implement this
        return new AuthenticationResponse();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        //TODO implement this
        return new AuthenticationResponse();
    }

    public boolean isLoggedIn() {

        //TODO implement this
        return true;
    }

    public User getCurrentUser() {

        //TODO implement this
       return new User();
    }
}
