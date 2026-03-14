package com.ironlogic.common.exception;

import com.ironlogic.common.enums.ApiCode;

public class BusinessException extends BaseException {

    public BusinessException(String message) {
        super(ApiCode.BUSINESS_ERROR, message);
    }
}
