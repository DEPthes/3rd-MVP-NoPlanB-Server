package com.noplanb.global.payload.exception;

import com.noplanb.global.payload.ErrorCode;

public class QuestNotFoundException extends NotFoundException{
    public QuestNotFoundException(){
        super(ErrorCode.QUEST_NOT_FOUND);
    }
}
