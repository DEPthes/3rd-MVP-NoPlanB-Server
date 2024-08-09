package com.noplanb.global.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E1", "올바르지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E2", "잘못된 HTTP 메서드를 호출했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E3", "서버 에러가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E4", "존재하지 않는 엔티티입니다."),
    CHARACTER_NOT_FOUND(HttpStatus.NOT_FOUND, "A1", "존재하지 않는 캐릭터입니다."),
    QUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "A2", "존재하지 않는 퀘스트입니다.");


    private final String message;

    private final String code;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
//        System.out.println("message = " + message);
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
