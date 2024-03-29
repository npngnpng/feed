package com.example.feed.domain.user.service;

import com.example.feed.domain.auth.controller.dto.request.LoginRequest;
import com.example.feed.domain.auth.controller.dto.response.TokenResponse;
import com.example.feed.domain.auth.exception.InvalidPasswordException;
import com.example.feed.domain.user.domain.User;
import com.example.feed.domain.user.facade.UserFacade;
import com.example.feed.global.security.jwt.JwtTokenProvider;
import com.example.feed.global.security.jwt.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserFacade userFacade;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse execute(LoginRequest request) {
        User user = userFacade.getUserByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw InvalidPasswordException.EXCEPTION;
        }

        String accessToken = jwtTokenProvider.generateAccessToken(request.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(request.getEmail());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .accessExp(jwtTokenProvider.getExp(TokenType.ACCESS))
                .refreshToken(refreshToken)
                .refreshExp(jwtTokenProvider.getExp(TokenType.REFRESH))
                .build();
    }
}
