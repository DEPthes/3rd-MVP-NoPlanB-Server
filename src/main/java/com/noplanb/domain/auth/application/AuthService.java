package com.noplanb.domain.auth.application;

import com.noplanb.domain.auth.domain.Token;
import com.noplanb.domain.auth.dto.response.LoginResponse;
import com.noplanb.domain.auth.repository.TokenRepository;
import com.noplanb.domain.user.domain.Provider;
import com.noplanb.domain.user.domain.Role;
import com.noplanb.domain.user.domain.User;
import com.noplanb.domain.user.repository.UserRepository;
import com.noplanb.global.config.security.util.JwtTokenUtil;
import com.noplanb.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final IdTokenVerifier idTokenVerifier;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public String verifyIdTokenAndExtractUsername(String idToken, String email) {
        if (idToken == null || idToken.trim().isEmpty()) {
            throw new IllegalArgumentException("ID 토큰은 null이거나 비어 있을 수 없습니다");
        }
        return idTokenVerifier.verifyIdToken(idToken, email);
    }

    public ResponseEntity<?> loginWithIdToken(String idToken, String email) {
        String username = verifyIdTokenAndExtractUsername(idToken, email);
        if (username != null) {
            String accessToken = jwtTokenUtil.generateToken(new HashMap<>(), username);
            String refreshToken = jwtTokenUtil.generateRefreshToken(new HashMap<>(), username);

            // Refresh token을 DB에 저장
            Token tokenEntity = Token.builder()
                    .email(email)
                    .refreshToken(refreshToken)
                    .build();
            tokenRepository.save(tokenEntity);

            // 사용자 정보를 DB에 저장
            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isEmpty()) {
                User user = User.builder()
                        .userName(username)
                        .email(email)
                        .password(null)
                        .provider(Provider.kakao)
                        .role(Role.USER)
                        .build();
                userRepository.save(user);
            }

            LoginResponse loginResponse = LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            ApiResponse apiResponse = ApiResponse.builder()
                    .check(true)
                    .information(loginResponse)
                    .build();

            return ResponseEntity.ok(apiResponse);

        } else {
            throw new RuntimeException("유효하지 않은 ID 토큰");
        }
    }
}