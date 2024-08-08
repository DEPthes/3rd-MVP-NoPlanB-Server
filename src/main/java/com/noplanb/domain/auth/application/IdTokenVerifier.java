package com.noplanb.domain.auth.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class IdTokenVerifier {

    @Value("${app.auth.kakao.userInfoUri}")
    private String kakaoUserInfoUri;

    private static final Logger logger = LoggerFactory.getLogger(IdTokenVerifier.class);
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public String verifyIdToken(String idToken, String email) {
        try {
            // HTTP 헤더에 Authorization 추가
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + idToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 카카오 사용자 정보 요청
            ResponseEntity<String> response = restTemplate.postForEntity(kakaoUserInfoUri, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode userInfo = objectMapper.readTree(response.getBody());
                String tokenEmail = userInfo.path("kakao_account").path("email").asText();
                // 사용자 정보 확인
                if (tokenEmail == null || tokenEmail.isEmpty()) {
                    logger.error("Kakao 계정에서 이메일을 찾을 수 없습니다.");
                    throw new IllegalArgumentException("ID 토큰에서 이메일을 추출할 수 없습니다.");
                }

                if (!email.equals(tokenEmail)) {
                    logger.error("ID 토큰의 이메일과 요청된 이메일이 일치하지 않습니다.");
                    throw new IllegalArgumentException("ID 토큰의 이메일과 요청된 이메일이 일치하지 않습니다.");
                }

                String nickname = userInfo.path("properties").path("nickname").asText();

                return tokenEmail;
            } else {
                logger.error("ID 토큰 검증 실패: HTTP 상태 " + response.getStatusCode());
                throw new RuntimeException("ID 토큰 검증 실패");
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                logger.error("ID 토큰이 만료되었습니다.", e);
                throw new RuntimeException("ID 토큰이 만료되었습니다.", e);
            } else {
                logger.error("ID 토큰 검증 실패", e);
                throw new RuntimeException("ID 토큰 검증 실패", e);
            }
        } catch (Exception e) {
            logger.error("ID 토큰 검증 실패", e);
            throw new RuntimeException("ID 토큰 검증 실패", e);
        }
    }
}