package com.chriskocabas.redditclone.controller;

import com.chriskocabas.redditclone.Exceptions.ValidationExceptions;
import com.chriskocabas.redditclone.dto.AuthenticationResponse;
import com.chriskocabas.redditclone.dto.LoginRequest;
import com.chriskocabas.redditclone.dto.RefreshTokenRequest;
import com.chriskocabas.redditclone.dto.RegisterRequest;
import com.chriskocabas.redditclone.repository.IUserRepository;
import com.chriskocabas.redditclone.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.OK;
import com.chriskocabas.redditclone.service.AuthService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final IUserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest,BindingResult bindingResult) {

        //check for validation errors
        Optional <String> validationErrors = ValidationExceptions.processValidationErrors(bindingResult);
        if (validationErrors.isPresent()){
            return new ResponseEntity<>(validationErrors.get(), HttpStatus.BAD_REQUEST);
        }

        Boolean usernamePresent = userRepository.findByUsername(registerRequest.getUsername()).isPresent();
        Boolean emailPresent = userRepository.findByEmail(registerRequest.getEmail()).isPresent();
        if(usernamePresent || emailPresent){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    usernamePresent ? "Username " + registerRequest.getUsername() +" is already taken."
                            : registerRequest.getEmail() + " is taken.");
        }

        authService.register(registerRequest);
        return new ResponseEntity<>("User Registration Successful",
                HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {

        return authService.login(loginRequest);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        System.out.println("Refresh token triggered" + refreshTokenRequest);
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }
}