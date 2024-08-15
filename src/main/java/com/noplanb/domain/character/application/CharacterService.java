package com.noplanb.domain.character.application;

import com.noplanb.domain.character.domain.Character;
// import com.noplanb.domain.character.dto.request.NewCharacterReq;
import com.noplanb.domain.character.dto.request.MyCharacterEquipItemDetailReq;
import com.noplanb.domain.character.dto.request.MyCharacterEquipItemReq;
import com.noplanb.domain.character.dto.request.NewCharacterReq;
import com.noplanb.domain.character.dto.request.UpdateNameReq;
import com.noplanb.domain.character.dto.response.InitialCharacterInfoRes;
import com.noplanb.domain.character.dto.response.MyCharacterInfoRes;
import com.noplanb.domain.character.dto.response.MyCharaterDetailRes;
import com.noplanb.domain.character.dto.response.MyCharaterListRes;
import com.noplanb.domain.character.repository.CharacterRepository;
import com.noplanb.domain.item.domain.Item;
import com.noplanb.domain.item.repository.ItemRepository;
import com.noplanb.domain.item_image.domain.ItemImage;
import com.noplanb.domain.item_image.domain.ItemType;
import com.noplanb.domain.item_image.domain.repository.ItemImageRepository;
import com.noplanb.domain.user.domain.User;
import com.noplanb.domain.user.repository.UserRepository;
import com.noplanb.global.config.security.token.UserPrincipal;
import com.noplanb.global.payload.ApiResponse;
import com.noplanb.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final ItemImageRepository itemImageRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    // 캐릭터 보여주기 메소드
    public MyCharaterListRes getMyCharacterDetail(UserPrincipal userPrincipal) {
        Character character = characterRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

        //유저의 item 중 is_equipped가 true인 것을 찾아서 item type과 함께 반환
        List<Item> items = character.getItems();

        // item status가 true인 것만 필터링 후 item type 정보와 함께 Response
        // 장착 중인 아이템만 필터링 (item status가 true)
        List<Item> quippedItems = items.stream().filter(Item::isEquipped).toList();

        List<MyCharaterDetailRes> myCharaterDetailResList = quippedItems.stream().map(item -> MyCharaterDetailRes.builder()
                .itemType(item.getItemImage().getItemType())
                .itemImage(item.getItemImage().getItemImageUrl())
                .build()).toList();

        MyCharaterListRes myCharaterListRes = MyCharaterListRes.builder()
                .myCharaterDetailResList(myCharaterDetailResList)
                .build();

        return myCharaterListRes;
    }

    // 캐릭터 상태만을 그대로 반환
    public ResponseEntity<?> getMyCharacter(UserPrincipal userPrincipal){
        MyCharaterListRes myCharaterListRes = getMyCharacterDetail(userPrincipal);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(myCharaterListRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 캐릭터 상태 + 이름 + 성장 시작일 + 총 경험치 + 달성한 전체 퀘스트 개수 + 성장일 반환
    public ResponseEntity<?> getMyCharacterInfo(UserPrincipal userPrincipal){
        // 캐릭터 상태
        MyCharaterListRes myCharaterListRes = getMyCharacterDetail(userPrincipal); //캐릭터 상세(아이템 장착 정보) 가져오기 메소드 호출

        // 아이템 장착 이외 정보를 가져오기 위한 캐릭터 객체 불러오기
        Character character = characterRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

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

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(myCharacterInfoRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @Transactional
    public ResponseEntity<?> updateCharacterName(UserPrincipal userPrincipal, UpdateNameReq updateNameReq) {
        Character character = characterRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

        String newCharacterName = updateNameReq.getNewCharacterName();
        character.updateCharacterName(newCharacterName);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("캐릭터 이름이 수정되었습니다.").build())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> getInitialCharacterInfo(UserPrincipal userPrincipal) {
        Character character = characterRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

        // 캐릭터 이름
        String characterName = character.getCharacterName();
        // 캐릭터 착장 정보 메소드 사용
        MyCharaterListRes myCharaterListRes = getMyCharacterDetail(userPrincipal);

        InitialCharacterInfoRes initialCharacterInfoRes = InitialCharacterInfoRes.builder()
                .characterName(characterName)
                .myCharaterDetailResList(myCharaterListRes.getMyCharaterDetailResList())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(initialCharacterInfoRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> createInitialCharacter(UserPrincipal userPrincipal, NewCharacterReq newCharacterReq) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        List<ItemImage> items = itemImageRepository.findAll();

        // 캐릭터 객체 생성
        Character character = Character.builder()
                .characterName(newCharacterReq.getCharacterName())
                .totalExp(0L)
                .totalQuest(0L)
                .todayExp(0L)
                .level(1L)
                .user(user)
                .quests(new ArrayList<>())
                .items(new ArrayList<>())
                .build();
        characterRepository.save(character);

        // 아이템 객체 생성 및 저장
        List<Item> itemList = new ArrayList<>();
        for (ItemImage itemImage : items) {
            Item item = Item.builder()
                    .character(character)
                    .isEquipped(false) // 장착 여부 초기화
                    .itemImage(itemImage)
                    .build();
            itemList.add(item);
        }
        itemRepository.saveAll(itemList);

        // 기본 아이템 장착 (요청값에 따라 다르게 설정)
        // 피부색(1~3) / 눈(4~6) / 머리(7~9) / 옷(10~11) 순서
        // 장착할 아이템 ID = 아이템 이미지의 ID

        for (Long equippedItemId : newCharacterReq.getItemIdList()) {
            Item item = itemList.stream()
                    .filter(i -> i.getItemImage().getId().equals(equippedItemId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("아이템을 찾을 수 없습니다."));
            item.updateEquipped(true);
        }
        itemRepository.saveAll(itemList); // 변경사항 저장

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("캐릭터가 생성 및 기본 아이템 장착이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    public ResponseEntity<?> myCharacterEquipItem(UserPrincipal userPrincipal, MyCharacterEquipItemReq myCharacterEquipItemReq) {
        Character character = characterRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        List<Item> item = character.getItems();
        // 캐릭터의 아이템 리스트
        List<MyCharacterEquipItemDetailReq> itemIdList = myCharacterEquipItemReq.getMyCharacterEquipItemDetailReqList();

        // 캐릭터의 아이템 리스트 중 장착 여부를 변경할 아이템 찾기 (아이템 타입, 아이템 id)
        // 해당 타입의 기존 장착 아이템은 장착 해제

        for (MyCharacterEquipItemDetailReq myCharacterEquipItemDetailReq : itemIdList) {
            ItemType itemType = myCharacterEquipItemDetailReq.getItemType();
            Long itemId = myCharacterEquipItemDetailReq.getItemId();

//            // 장착 해제할 아이템 찾기 후 isEquipped = false로 변경
//            Item itemToUnequip = item.stream()
//                    .filter(i -> i.getItemImage().getItemType().equals(itemType) && i.isEquipped())
//                    .findFirst()
//                    .orElseThrow(() -> new IllegalArgumentException("장착 해제할 아이템을 찾을 수 없습니다."));
//            itemToUnequip.updateEquipped(false);

            // 모든 아이템 장착 상태를 isEquipped = false로 초기화
            for (Item i : item) {
                i.updateEquipped(false);
            }

            // 장착할 아이템 isEquipped = true로 변경
            Item itemToEquip = item.stream()
                    .filter(i -> i.getItemImage().getId().equals(itemId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("장착할 아이템을 찾을 수 없습니다."));
            itemToEquip.updateEquipped(true);

        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("아이템 장착이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
