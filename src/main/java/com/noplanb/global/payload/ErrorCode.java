package com.noplanb.global.payload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_PARAMETER(HttpStatus.valueOf(400), null, "잘못된 요청 데이터 입니다."),
    INVALID_REPRESENTATION(HttpStatus.valueOf(400), null, "잘못된 표현 입니다."),
    INVALID_FILE_PATH(HttpStatus.valueOf(400), null, "잘못된 파일 경로 입니다."),
    INVALID_OPTIONAL_ISPRESENT(HttpStatus.valueOf(400), null, "해당 값이 존재하지 않습니다."),
    INVALID_CHECK(HttpStatus.valueOf(400), null, "해당 값이 유효하지 않습니다."),
    INVALID_AUTHENTICATION(HttpStatus.valueOf(400), null, "잘못된 인증입니다."),
    INVALID_TOKEN(HttpStatus.valueOf(400), null, "잘못된 토큰입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E1", "올바르지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E2", "잘못된 HTTP 메서드를 호출했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E3", "서버 에러가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E4", "존재하지 않는 엔티티입니다."),
    CHARACTER_NOT_FOUND(HttpStatus.NOT_FOUND, "A1", "존재하지 않는 캐릭터입니다.");


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
