package com.ironlogic.common.enums;

public enum ApiCode {
    SUCCESS(0, "OK"),
    BAD_REQUEST(40000, "Bad request"),
    BUSINESS_ERROR(40001, "Business error"),
    INTERNAL_ERROR(50000, "Internal server error");

    private final int code;
    private final String message;

    ApiCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
