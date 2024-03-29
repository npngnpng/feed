package com.example.feed.domain.user.facade;

import com.example.feed.domain.auth.domain.AuthCode;
import com.example.feed.domain.user.domain.repository.AuthCodeRepository;
import com.example.feed.domain.user.exception.BadAuthCodeException;
import com.example.feed.domain.user.exception.UnverifiedEmailException;
import com.example.feed.global.jms.JmsProperties;
import com.example.feed.global.jms.JmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@RequiredArgsConstructor
@Component
public class AuthCodeFacade {

    private static final Random RANDOM = new Random();
    private final AuthCodeRepository authCodeRepository;
    private final JmsUtil jmsUtil;
    private final JmsProperties jmsProperties;

    public void verifyAuthCode(String code, String email) {
        AuthCode authCode = authCodeRepository.findById(email)
                .filter(a ->  a.getCode().equals(code))
                .orElseThrow(() -> BadAuthCodeException.EXCEPTION);
        authCode.verify();
        authCodeRepository.save(authCode);
    }

    public void checkIsVerified(String email) {
        authCodeRepository.findById(email)
                .filter(AuthCode::isVerified)
                .orElseThrow(() -> UnverifiedEmailException.EXCEPTION);
    }

    public void sendMail(String email) {
        String code = createRandomCode();
        AuthCode authCode = getAuthCode(email, code);
        authCodeRepository.save(authCode);

        jmsUtil.sendMail(email, authCode.getCode());
    }

    private AuthCode getAuthCode(String email, String code) {
        AuthCode authCode = authCodeRepository.findById(email)
                .orElseGet(() -> createAuthCode(email, code));

        authCode.updateAuthCode(code, jmsProperties.getAuthExp());
        return authCode;
    }

    private AuthCode createAuthCode(String email, String code) {
        return AuthCode.builder()
                .email(email)
                .code(code)
                .isVerified(false)
                .ttl(jmsProperties.getAuthExp())
                .build();
    }

    private String createRandomCode() {
        return String.format("%06d", RANDOM.nextInt(1000000) % 1000000);
    }
}
