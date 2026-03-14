package com.ironlogic.common.exception;

import com.ironlogic.common.enums.ApiCode;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        super(ApiCode.FORBIDDEN, HttpStatus.FORBIDDEN, message);
    }
}
