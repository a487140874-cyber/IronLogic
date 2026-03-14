package com.ironlogic.common.exception;

import com.ironlogic.common.enums.ApiCode;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(ApiCode.NOT_FOUND, HttpStatus.NOT_FOUND, message);
    }
}
