package com.noplanb.domain.auth.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noplanb.domain.auth.dto.request.SignInReq;
import com.noplanb.domain.auth.dto.response.AuthRes;
import com.noplanb.domain.user.domain.User;
import com.noplanb.domain.user.repository.UserRepository;
import com.noplanb.global.config.security.token.UserPrincipal;
import com.noplanb.global.error.DefaultException;
import com.noplanb.global.payload.ApiResponse;
import com.noplanb.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<ApiResponse> signIn(SignInReq signInReq) {
        logger.debug("Sign in request received: {}", signInReq);

        String accessToken = signInReq.getAccessToken();
        String providerId = validateAccessToken(accessToken);

        Optional<User> optionalUser = userRepository.findByEmail(signInReq.getEmail());
        if (!optionalUser.isPresent()) {
            logger.error("유저 정보를 찾을 수 없습니다: {}", signInReq.getEmail());
            throw new DefaultException(ErrorCode.INVALID_CHECK, "유저 정보가 유효하지 않습니다.");
        }

        User user = optionalUser.get();
        logger.debug("Found user: {}", user);

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, // UserPrincipal 객체 생성
                null,
                userPrincipal.getAuthorities() // UserPrincipal에서 권한 정보 가져오기
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AuthRes authResponse = AuthRes.builder()
                .accessToken(accessToken)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(authResponse)
                .build();

        logger.debug("Sign in response: {}", apiResponse);
        return ResponseEntity.ok(apiResponse);
    }

    private String validateAccessToken(String accessToken) {
        String userInfoEndpoint = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(userInfoEndpoint, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                // 사용자 정보를 파싱하여 providerId 추출
                return parseProviderId(response.getBody());
            } else {
                throw new DefaultException(ErrorCode.INVALID_CHECK, "토큰 검증에 실패하였습니다.");
            }
        } catch (Exception e) {
            logger.error("토큰 검증 중 오류 발생: {}", e.getMessage(), e);
            throw new DefaultException(ErrorCode.INVALID_CHECK, "토큰 검증 중 오류가 발생하였습니다.");
        }
    }

    private String parseProviderId(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.path("id").asText();
        } catch (Exception e) {
            logger.error("JSON 파싱 중 오류 발생: {}", e.getMessage(), e);
            throw new DefaultException(ErrorCode.INVALID_CHECK, "JSON 파싱 중 오류가 발생하였습니다.");
        }
    }
}
