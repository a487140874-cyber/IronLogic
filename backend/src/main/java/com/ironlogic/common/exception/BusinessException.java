package com.ironlogic.common.exception;

import com.ironlogic.common.enums.ApiCode;
import org.springframework.http.HttpStatus;

public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(ApiCode.BUSINESS_ERROR, HttpStatus.BAD_REQUEST, message);
    }
}
