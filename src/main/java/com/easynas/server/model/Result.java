package com.easynas.server.model;

import lombok.Getter;

import javax.annotation.Nullable;

/**
 * 请求返回结果类型
 *
 * @author liangyongrui
 */
@Getter
public class Result<T> {
    private final T data;
    private final int code;
    private final String message;

    private Result(T data, int code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(@Nullable T data) {
        return new Result<>(data, 200, "success");
    }

    public static <T> Result<T> fail(@Nullable String message) {
        return new Result<>(null, 400, message);
    }
}
