package com.coopbank.selfonboarding.security.util;

import com.coopbank.selfonboarding.entity.JwtUser;
import com.coopbank.selfonboarding.repository.JwtUserRepository;
import com.coopbank.selfonboarding.response.ErrorDto;
import com.coopbank.selfonboarding.security.domain.JwtResponseDto;
import com.coopbank.selfonboarding.service.JwtUserService;
import com.coopbank.selfonboarding.service.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    JwtUserRepository jwtUserRepository;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//        UserDetails principal = (UserDetails) authentication.getPrincipal();
//        Optional<JwtUser> userOptional = jwtUserRepository.findJwtUserByUsername(principal.getUsername());
//        var user = jwtUserRepository.findJwtUserByUsername(principal.getUsername());
//        String token = jwtUtils.createJwt(user.getEmail(),user.getUsername(),user.getRoleName(),user.getAuthorities());
//        String refreshToken = refreshTokenService.createToken(user);
//        response.addHeader("Authorization", "Bearer " + token);
//        response.addHeader("Content-Type", "application/json");
//        response.getWriter().write(objectMapper.writeValueAsString(JwtResponseDto.of(token, refreshToken)));
//    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Optional<JwtUser> userOptional = jwtUserRepository.findJwtUserByUsername(principal.getUsername());

        if (userOptional.isPresent()) {
            JwtUser user = userOptional.get();
            String token = jwtUtils.createJwt(user.getEmail(), user.getUsername(), user.getRoleName(), user.getAuthorities());
            String refreshToken = refreshTokenService.createToken(user);
            response.addHeader("Authorization", "Bearer " + token);
            response.addHeader("Content-Type", "application/json");
            response.getWriter().write(objectMapper.writeValueAsString(JwtResponseDto.of(token, refreshToken)));
        } else {
            // Handle the case when the user is not found
        	 response.setStatus(HttpServletResponse.SC_NOT_FOUND); // HTTP 404
             response.setContentType("application/json");
             ErrorDto error = new ErrorDto("User not found", "The requested user does not exist.", 404);
             response.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }

}