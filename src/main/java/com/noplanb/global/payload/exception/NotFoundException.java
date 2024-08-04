package com.noplanb.global.payload.exception;

import com.noplanb.global.payload.ErrorCode;

public class NotFoundException extends BusinessBaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}
