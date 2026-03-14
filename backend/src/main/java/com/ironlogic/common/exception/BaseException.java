package com.ironlogic.common.exception;

import com.ironlogic.common.enums.ApiCode;
import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {

    private final ApiCode apiCode;
    private final HttpStatus httpStatus;

    protected BaseException(ApiCode apiCode, HttpStatus httpStatus, String message) {
        super(message);
        this.apiCode = apiCode;
        this.httpStatus = httpStatus;
    }

    public ApiCode getApiCode() {
        return apiCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
