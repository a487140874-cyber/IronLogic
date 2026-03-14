package com.ironlogic.common.api;

import com.ironlogic.common.enums.ApiCode;
import java.time.Instant;

public record ApiResponse<T>(
        int code,
        String message,
        T data,
        Instant timestamp
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ApiCode.SUCCESS.getCode(), ApiCode.SUCCESS.getMessage(), data, Instant.now());
    }

    public static ApiResponse<Void> success() {
        return success(null);
    }

    public static ApiResponse<Void> failure(ApiCode apiCode, String message) {
        return new ApiResponse<>(apiCode.getCode(), message, null, Instant.now());
    }
}
