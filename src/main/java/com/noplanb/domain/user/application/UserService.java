package com.noplanb.domain.user.application;

import com.noplanb.domain.user.domain.User;
import com.noplanb.domain.user.dto.response.MyAccountRes;
import com.noplanb.domain.user.repository.UserRepository;
import com.noplanb.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    final UserRepository userRepository;

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public ResponseEntity<?> getMyAccount(UserPrincipal userPrincipal) {
        User user=userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        MyAccountRes myAccountRes = MyAccountRes.builder()
                .email(user.getEmail())
                .build();
        return ResponseEntity.ok(myAccountRes);
    }

}
