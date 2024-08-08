package com.noplanb.domain.character.application;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.character.dto.response.MyCharacterInfoRes;
import com.noplanb.domain.character.dto.response.MyCharaterDetailRes;
import com.noplanb.domain.character.dto.response.MyCharaterListRes;
import com.noplanb.domain.character.repository.CharacterRepository;
import com.noplanb.domain.item.domain.Item;
import com.noplanb.domain.item.repository.ItemRepository;
import com.noplanb.domain.item_image.domain.repository.ItemImageRepository;
import com.noplanb.domain.user.domain.User;
import com.noplanb.domain.user.repository.UserRepository;
import com.noplanb.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;

    // 캐릭터 보여주기 메소드
    public MyCharaterListRes getMyCharacterDetail(UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        //유저의 item 중 is_equipped가 true인 것을 찾아서 item tyoe과 함께 반환
        Character character = characterRepository.findByUser(user);
        List<Item> items = itemRepository.findByCharacter(character);

        // item status가 true인 것만 필터링 후 item type 정보와 함께 Response

        // 장착 중인 아이템만 필터링
        List<Item> quippedItems = items.stream().filter(Item::isEquipped).toList();
        List<MyCharaterDetailRes> myCharaterDetailResList = quippedItems.stream().map(item -> MyCharaterDetailRes.builder()
                .itemType(item.getItemType())
                //item image 찾기
                .itemImage(itemImageRepository.findItemImageByItem(item))
                .build()).toList();

        MyCharaterListRes myCharaterListRes = MyCharaterListRes.builder()
                .myCharaterDetailResList(myCharaterDetailResList)
                .build();

        return myCharaterListRes;
    }

    // 캐릭터 상태만을 그대로 반환
    public ResponseEntity<?> getMyCharacter(UserPrincipal userPrincipal){
        MyCharaterListRes myCharaterListRes = getMyCharacterDetail(userPrincipal);
        return ResponseEntity.ok(myCharaterListRes);
    }

    // 캐릭터 상태 + 이름 + 성장 시작일 + 총 경험치 + 달성한 전체 퀘스트 개수 + 성장일 반환
    public ResponseEntity<?> getMyCharacterInfo(UserPrincipal userPrincipal){
        //캐릭터 상태
        MyCharaterListRes myCharaterListRes = getMyCharacterDetail(userPrincipal);

        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Character character = characterRepository.findByUser(user);

        // 캐릭터 이름
        String characterName = character.getCharacterName();
        // 성장 시작일
        LocalDateTime characterStartDate = character.getCreatedAt();
        // 총 경험치
        Long totalExp = character.getTotalExp();
        // 달성한 전체 퀘스트 개수
        Long totalQuest = character.getTotalQuest();
        // 성장일 - 성장 시작일로부터의 일수 계산 (당일은 1일)
        int growDate = LocalDateTime.now().getDayOfYear() - characterStartDate.getDayOfYear() + 1;

        MyCharacterInfoRes myCharacterInfoRes = MyCharacterInfoRes.builder()
                .characterName(characterName)
                .startDate(characterStartDate)
                .totalExp(totalExp)
                .totalQuest(totalQuest)
                .growDate(growDate)
                .myCharaterDetailResList(myCharaterListRes.getMyCharaterDetailResList())
                .build();

        return ResponseEntity.ok(myCharacterInfoRes);
    }


}
