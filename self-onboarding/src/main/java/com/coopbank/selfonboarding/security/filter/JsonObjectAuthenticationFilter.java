package com.coopbank.selfonboarding.security.filter;

import com.coopbank.selfonboarding.security.domain.LoginCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
@Slf4j
public class JsonObjectAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            LoginCredentials authRequest = objectMapper.readValue(sb.toString(), LoginCredentials.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(), authRequest.getPassword()
            );
            log.info("Testing Here  ####################################");
            setDetails(request, token);
            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            log.info("Failed Here  ####################################"+e+"###################################");
            throw new RuntimeException(e);
        }
    }

}
