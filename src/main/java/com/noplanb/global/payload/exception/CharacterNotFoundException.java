package com.noplanb.global.payload.exception;

import com.noplanb.global.payload.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CharacterNotFoundException extends NotFoundException{
    public CharacterNotFoundException(){
//        log.info("zz");
        super(ErrorCode.CHARACTER_NOT_FOUND);
    }
}
