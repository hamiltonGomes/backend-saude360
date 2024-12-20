package com.saude360.backendsaude360.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.saude360.backendsaude360.data.UserDetailsSecurity;
import com.saude360.backendsaude360.exceptions.TokenGenerationException;
import com.saude360.backendsaude360.exceptions.TokenInvalidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JWTToken {

    @Value("${api.token.secret}")
    private String secret;

    public String generateToken(UserDetailsSecurity user) {
        try {
            var algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("api-saude360")
                    .withSubject(user.getUsername())
                    .withExpiresAt(getTokenExpiration())
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new TokenGenerationException("Error generating the token", e);
        }

    }

    public String validateToken(String token) {
        var algorithm = Algorithm.HMAC256(secret);

        try {
            return JWT.require(algorithm)
                    .withIssuer("api-saude360")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new TokenInvalidException(e.getMessage());
        }
    }

    private Instant getTokenExpiration() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}