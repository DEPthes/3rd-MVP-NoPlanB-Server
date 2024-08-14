package com.noplanb.domain.item.application;

import com.noplanb.domain.character.domain.Character;
import com.noplanb.domain.character.repository.CharacterRepository;
import com.noplanb.domain.item.dto.response.CategoryItemListRes;
import com.noplanb.domain.item.domain.Item;
import com.noplanb.domain.item.dto.response.CategoryItemRes;
import com.noplanb.domain.item_image.domain.repository.ItemImageRepository;
import com.noplanb.global.config.security.token.UserPrincipal;
import com.noplanb.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.noplanb.domain.item_image.domain.ItemType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final CharacterRepository characterRepository;
    private final ItemImageRepository itemImageRepository;


    public ResponseEntity<?> getHairItemList(UserPrincipal userPrincipal) {
        return getCategoryItemList(userPrincipal, "hair");
    }

    public ResponseEntity<?> getFaceItemList(UserPrincipal userPrincipal) {
        return getCategoryItemList(userPrincipal, "face");
    }

    public ResponseEntity<?> getFashionItemList(UserPrincipal userPrincipal) {
        return getCategoryItemList(userPrincipal, "fashion");
    }

    public ResponseEntity<?> getBackgroundItemList(UserPrincipal userPrincipal) {
        return getCategoryItemList(userPrincipal, "background");
    }

    public ResponseEntity<?> getCategoryItemList(UserPrincipal userPrincipal, String category) {
        Character character = characterRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        List<Item> items = character.getItems();
        List<Item> itemList = null;

        switch (category) {
            case "hair":
                // item_type이 HAIR인 item만 필터링
                itemList = items.stream()
                        .filter(item -> item.getItemImage().getItemType().equals(HAIR))
                        .toList();
                break;

            case "face":
                // item_type이 EYE, FACECOLOR인 item만 필터링
                itemList = items.stream()
                        .filter(item -> item.getItemImage().getItemType().equals(EYE) || item.getItemImage().getItemType().equals(FACECOLOR))
                        .toList();
                break;


            case "fashion":
                // item_type이 HEAD, GLASSES, CLOTHES인 item만 필터링
                itemList = items.stream()
                        .filter(item -> item.getItemImage().getItemType().equals(HEAD) || item.getItemImage().getItemType().equals(GLASSES) || item.getItemImage().getItemType().equals(CLOTHES))
                        .toList();
                break;

            case "background":
                // item_type이 BACKGROUND인 item만 필터링
                itemList = items.stream()
                        .filter(item -> item.getItemImage().getItemType().equals(BACKGROUND))
                        .toList();
                break;
        }

        List<CategoryItemRes> categoryItemRes=itemList.stream().map(item -> CategoryItemRes.builder()
                .itemId(item.getId())
                .itemImage(item.getItemImage().getItemImageUrl())
                .itemName(item.getItemImage().getItemName())
                .itemType(item.getItemImage().getItemType())
                // 장착 가능 여부 -> 캐릭터의 레벨이 아이템의 필요 레벨보다 높거나 같으면 장착 가능
                .ableToEquip(character.getLevel() >= item.getItemImage().getRequiredLevel()||item.isEquipped())
                .isEquipped(item.isEquipped())
                .requiredLevel(item.getItemImage().getRequiredLevel())
                .build()).toList();


        CategoryItemListRes categoryItemListRes = CategoryItemListRes.builder()
                .categoryItemList(categoryItemRes)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(categoryItemListRes)
                .build();

        return ResponseEntity.ok(apiResponse);

    }
}
