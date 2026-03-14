package com.ironlogic.common.exception;

import com.ironlogic.common.enums.ApiCode;

public abstract class BaseException extends RuntimeException {

    private final ApiCode apiCode;

    protected BaseException(ApiCode apiCode, String message) {
        super(message);
        this.apiCode = apiCode;
    }

    public ApiCode getApiCode() {
        return apiCode;
    }
}
