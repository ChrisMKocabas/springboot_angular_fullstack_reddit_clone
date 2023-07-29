package com.chriskocabas.redditclone.security;

import com.chriskocabas.redditclone.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return generateTokenWithUserName(principal.getUsername());
    }

    public String generateTokenWithUserName(String username) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtExpirationInMillis))
                .subject(username)
                .claim("scope", Role.USER)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Date.from(jwtDecoder.decode(token).getExpiresAt());
    }


    public String extractUsername(String token) {
        Jwt parsedJwt = jwtDecoder.decode(token);
        return parsedJwt.getSubject();

        //switched from extracting claims to jwt decoder
//        return extractClaim(jwt,Jwt::getSubject);
    }


    public <T> T extractClaim(String jwt, Function<Jwt,T> claimsResolver){
        final Jwt claims = jwtDecoder.decode(jwt);

        return claimsResolver.apply(claims);

    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }

}