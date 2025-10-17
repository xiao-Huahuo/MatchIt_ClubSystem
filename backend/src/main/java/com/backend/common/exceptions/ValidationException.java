package com.backend.common.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 数据校验失败（如重复注册）时抛出的异常
 */
public class ValidationException extends RuntimeException {

    private final static Integer DEFAULT_CODE = HttpStatus.BAD_REQUEST.value(); // 400
    private final Integer code = DEFAULT_CODE;

    public ValidationException(String message) {
        super(message);
    }

    public Integer getCode() {
        return code;
    }
}