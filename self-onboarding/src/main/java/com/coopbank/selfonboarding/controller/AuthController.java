package com.coopbank.selfonboarding.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coopbank.selfonboarding.security.domain.JwtRefreshRequestDto;
import com.coopbank.selfonboarding.security.domain.JwtResponseDto;
import com.coopbank.selfonboarding.service.RefreshTokenService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh")
    public JwtResponseDto refreshJwt(@RequestBody JwtRefreshRequestDto refreshRequestDto) {
        return refreshTokenService.refreshToken(refreshRequestDto);
    }

}
