package com.easynas.server.model;

import lombok.Data;

/**
 * 请求返回结果类型
 *
 * @author liangyongrui
 */
@Data
public class Result<T> {
    private T data;
    private int code;
    private String message;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "success";
        result.data = data;
        return result;
    }

    public static <T> Result<T> fail(String message) {
        Result<T> result = new Result<>();
        result.code = 400;
        result.message = message;
        return result;
    }
}
