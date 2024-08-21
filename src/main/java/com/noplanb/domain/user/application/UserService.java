package com.noplanb.domain.user.application;

import com.noplanb.domain.auth.domain.Token;
import com.noplanb.domain.auth.repository.TokenRepository;
import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.character.repository.CharacterRepository;
import com.noplanb.domain.quest.domain.DailyExperience;
import com.noplanb.domain.quest.repository.DailyExperienceRepository;
import com.noplanb.domain.user.domain.User;
import com.noplanb.domain.user.dto.response.MyAccountRes;
import com.noplanb.domain.user.dto.response.UserCharacterExistRes;
import com.noplanb.domain.user.repository.UserRepository;
import com.noplanb.global.config.security.token.UserPrincipal;
import com.noplanb.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final CharacterRepository characterRepository;
    private final DailyExperienceRepository dailyExperienceRepository;

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
    public ResponseEntity<?> getUserCharacterExist(UserPrincipal userPrincipal) {
        // 사용자 찾기
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 캐릭터 존재 여부 확인
        boolean isExist = characterRepository.existsByUser(user);

        UserCharacterExistRes userCharacterExistRes = UserCharacterExistRes.builder()
                .isExist(isExist)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userCharacterExistRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> cancelAccount(UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        String email = user.getEmail();

        // 토큰 정보 삭제
        Token token = tokenRepository.findByEmail(email);
        if (token != null) { tokenRepository.delete(token); }

        // 캐릭터 정보 삭제
        characterRepository.deleteByUser(user);

        // 유저 정보 삭제
        userRepository.delete(user);

        // Daily Experience 정보 삭제
        Character character = characterRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        Long characterId = character.getId();
        dailyExperienceRepository.deleteAllByCharacterId(characterId);

        // 응답 생성 및 반환
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("회원 탈퇴가 완료되었습니다.")
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
