package com.noplanb.domain.auth.application;


import com.noplanb.domain.user.domain.Provider;
import com.noplanb.domain.user.domain.Role;
import com.noplanb.domain.user.domain.User;
import com.noplanb.domain.user.repository.UserRepository;
import com.noplanb.global.DefaultAssert;
import com.noplanb.global.config.security.auth.OAuth2UserInfo;
import com.noplanb.global.config.security.auth.OAuth2UserInfoFactory;
import com.noplanb.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomDefaultOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (Exception e) {
            log.error("Error processing OAuth2 user: {}", e.getMessage(), e);  // 추가된 로깅
            DefaultAssert.isAuthentication(e.getMessage());
        }
        return null;
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        DefaultAssert.isAuthentication(!oAuth2UserInfo.getEmail().isEmpty());

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            DefaultAssert.isAuthentication(user.getProvider().equals(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId())));
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        log.debug("OAuth2 user processed: {}", user);  // 추가된 로깅
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
                .provider(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .email(oAuth2UserInfo.getEmail())
                .password(encodePassword(oAuth2UserInfo.getId()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        log.debug("New user registered: {}", savedUser);  // 추가된 로깅
        return savedUser;
    }

    private String encodePassword(String password) {
        // PasswordEncoder를 사용하여 비밀번호 인코딩
        return customPasswordEncoder().encode(password);
    }

    // PasswordEncoder를 Bean으로 등록하여 사용할 수 있도록 설정
    @Bean
    public PasswordEncoder customPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


