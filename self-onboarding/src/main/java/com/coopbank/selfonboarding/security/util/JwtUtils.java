package com.coopbank.selfonboarding.security.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.expiration}")
    private int expTime;

    @Value("${jwt.secret}")
    private String secret;

    public String createJwt(String email, String username, String roleName, Collection<? extends GrantedAuthority> authorities) {
        // Convert authorities to a list of strings
        List<String> authorityStrings = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return JWT.create()
                .withSubject(email)
                .withClaim("username",username)
                .withClaim("role",roleName)
                .withClaim("authorities", authorityStrings)
                .withExpiresAt(Instant.ofEpochMilli(ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli() + expTime))
                .sign(Algorithm.HMAC256(secret));
    }


}