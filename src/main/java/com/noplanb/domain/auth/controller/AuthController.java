package com.noplanb.domain.auth.controller;


import com.noplanb.domain.auth.domain.Token;
import com.noplanb.domain.auth.dto.request.SignInReq;
import com.noplanb.domain.auth.dto.response.AuthRes;
import com.noplanb.domain.auth.dto.response.KakaoUserResponse;
import com.noplanb.domain.auth.dto.response.TokenMapping;
import com.noplanb.domain.auth.repository.TokenRepository;
import com.noplanb.domain.user.domain.User;
import com.noplanb.domain.user.repository.UserRepository;
import com.noplanb.global.config.security.token.CurrentUser;
import com.noplanb.global.config.security.token.UserPrincipal;
import com.noplanb.global.config.security.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Value("${app.auth.kakaoApiUrl}")
    private String kakaoApiUrl;

    @PostMapping("/kakao/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody SignInReq signInReq) {
        String accessToken = signInReq.getAccessToken();
        String email = getEmailFromKakao(accessToken);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        User user = userOptional.get();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                UserPrincipal.create(user),
                null,
                UserPrincipal.create(user).getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenUtil.generateToken(((UserDetails) authentication.getPrincipal()).getUsername());
        String refreshToken = jwtTokenUtil.generateToken(((UserDetails) authentication.getPrincipal()).getUsername());

        // 토큰 저장 로직 추가
        Token token = Token.builder()
                .email(email)
                .refreshToken(refreshToken)
                .build();
        tokenRepository.save(token);

        TokenMapping tokenMapping = TokenMapping.builder()
                .email(email)
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .build();

        AuthRes authResponse = AuthRes.builder()
                .accessToken(tokenMapping.getAccessToken())
                .refreshToken(tokenMapping.getRefreshToken())
                .build();

        return ResponseEntity.ok(authResponse);
    }

    private String getEmailFromKakao(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                kakaoApiUrl,
                HttpMethod.GET,
                entity,
                KakaoUserResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            KakaoUserResponse kakaoUserResponse = response.getBody();
            return kakaoUserResponse.getKakaoAccount().getEmail();
        } else {
            throw new RuntimeException("Failed to get user info from Kakao API");
        }
    }

    @GetMapping("/kakao/sign-in")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userPrincipal);
    }
}